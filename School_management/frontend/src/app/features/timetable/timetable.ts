import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { DayOfWeek, SchoolClass, TimetableEntry } from '../../core/models/models';
import { AcademicService } from '../../core/services/academic.service';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-timetable',
  imports: [
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
  ],
  templateUrl: './timetable.html',
})
export class TimetableComponent implements OnInit {
  private auth = inject(AuthService);
  private academicService = inject(AcademicService);
  private userService = inject(UserService);

  readonly days: DayOfWeek[] = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'];
  readonly isTeacher = computed(() => this.auth.hasRole('TEACHER'));

  readonly classes = signal<SchoolClass[]>([]);
  readonly selectedClassId = signal<number | null>(null);
  readonly entries = signal<TimetableEntry[]>([]);
  readonly title = signal('Class Timetable');

  ngOnInit(): void {
    this.academicService.listClasses().subscribe((c) => {
      this.classes.set(c);
      if (c.length) {
        this.selectedClassIdModel = c[0].id;
      }
    });
  }

  get selectedClassIdModel(): number | null {
    return this.selectedClassId();
  }
  set selectedClassIdModel(value: number | null) {
    this.selectedClassId.set(value);
    if (value) {
      this.title.set('Class Timetable');
      this.academicService.timetableForClass(value).subscribe((e) => this.entries.set(e));
    }
  }

  entriesForDay(day: DayOfWeek): TimetableEntry[] {
    return this.entries()
      .filter((e) => e.dayOfWeek === day)
      .sort((a, b) => a.startTime.localeCompare(b.startTime));
  }

  loadMySchedule(): void {
    const userId = this.auth.currentUser()?.id;
    this.userService.listTeachers().subscribe((teachers) => {
      const me = teachers.find((t) => t.user.id === userId);
      if (me) {
        this.title.set('My Teaching Schedule');
        this.academicService.timetableForTeacher(me.id).subscribe((e) => this.entries.set(e));
      }
    });
  }

  formatTime(value: string): string {
    return value?.slice(0, 5) ?? '';
  }
}
