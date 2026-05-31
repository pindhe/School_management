import { DatePipe } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AttendanceRecord, AttendanceStatus, SchoolClass, StudentResponse } from '../../core/models/models';
import { AcademicService } from '../../core/services/academic.service';
import { AttendanceEntry, AttendanceService } from '../../core/services/attendance.service';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';

interface MarkRow {
  studentId: number;
  name: string;
  status: AttendanceStatus;
  remarks: string;
}

@Component({
  selector: 'app-attendance',
  imports: [
    DatePipe,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './attendance.html',
})
export class AttendanceComponent implements OnInit {
  private auth = inject(AuthService);
  private academicService = inject(AcademicService);
  private userService = inject(UserService);
  private attendanceService = inject(AttendanceService);
  private snackBar = inject(MatSnackBar);

  readonly statuses: AttendanceStatus[] = ['PRESENT', 'ABSENT', 'LATE', 'LEAVE'];
  readonly isStaff = computed(() => this.auth.hasRole('ADMIN', 'TEACHER'));
  readonly isParent = computed(() => this.auth.hasRole('PARENT'));

  // staff marking
  readonly classes = signal<SchoolClass[]>([]);
  readonly selectedClassId = signal<number | null>(null);
  readonly selectedDate = signal<string>(new Date().toISOString().slice(0, 10));
  readonly rows = signal<MarkRow[]>([]);

  // viewing
  readonly records = signal<AttendanceRecord[]>([]);
  readonly children = signal<StudentResponse[]>([]);

  get selectedClassIdModel(): number | null {
    return this.selectedClassId();
  }
  set selectedClassIdModel(value: number | null) {
    this.selectedClassId.set(value);
  }

  get selectedDateModel(): string {
    return this.selectedDate();
  }
  set selectedDateModel(value: string) {
    this.selectedDate.set(value);
  }

  ngOnInit(): void {
    if (this.isStaff()) {
      this.academicService.listClasses().subscribe((c) => this.classes.set(c));
    } else if (this.isParent()) {
      this.loadChildren();
    } else {
      this.attendanceService.myAttendance().subscribe((r) => this.records.set(r));
    }
  }

  private loadChildren(): void {
    const userId = this.auth.currentUser()?.id;
    this.userService.listParents().subscribe((parents) => {
      const me = parents.find((p) => p.user.id === userId);
      this.children.set(me?.children ?? []);
    });
  }

  loadRoster(): void {
    const classId = this.selectedClassId();
    if (!classId) return;
    this.userService.listStudents(classId).subscribe((students) => {
      this.attendanceService.classAttendance(classId, this.selectedDate()).subscribe((existing) => {
        const map = new Map(existing.map((e) => [e.studentId, e]));
        this.rows.set(
          students.map((s) => ({
            studentId: s.id,
            name: s.user.fullName,
            status: (map.get(s.id)?.status ?? 'PRESENT') as AttendanceStatus,
            remarks: map.get(s.id)?.remarks ?? '',
          })),
        );
      });
    });
  }

  save(): void {
    const entries: AttendanceEntry[] = this.rows().map((r) => ({
      studentId: r.studentId,
      status: r.status,
      remarks: r.remarks || undefined,
    }));
    if (!entries.length) return;
    this.attendanceService.mark(this.selectedDate(), entries).subscribe(() => {
      this.snackBar.open('Attendance saved', 'OK', { duration: 3000 });
    });
  }

  viewChild(studentId: number): void {
    this.attendanceService.studentAttendance(studentId).subscribe((r) => this.records.set(r));
  }
}
