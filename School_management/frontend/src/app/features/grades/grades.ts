import { DatePipe, NgTemplateOutlet } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  AcademicYear,
  GradeRecord,
  StudentResponse,
  Subject,
} from '../../core/models/models';
import { AcademicService } from '../../core/services/academic.service';
import { AuthService } from '../../core/services/auth.service';
import { GradeService } from '../../core/services/grade.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-grades',
  imports: [
    DatePipe,
    NgTemplateOutlet,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './grades.html',
})
export class GradesComponent implements OnInit {
  private auth = inject(AuthService);
  private gradeService = inject(GradeService);
  private academicService = inject(AcademicService);
  private userService = inject(UserService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  readonly isStaff = computed(() => this.auth.hasRole('ADMIN', 'TEACHER'));
  readonly isParent = computed(() => this.auth.hasRole('PARENT'));

  readonly students = signal<StudentResponse[]>([]);
  readonly subjects = signal<Subject[]>([]);
  readonly years = signal<AcademicYear[]>([]);
  readonly children = signal<StudentResponse[]>([]);
  readonly grades = signal<GradeRecord[]>([]);
  readonly viewStudentId = signal<number | null>(null);

  readonly gradeTypes = ['Assignment', 'Quiz', 'Midterm', 'Final Exam', 'Project'];

  readonly average = computed(() => {
    const list = this.grades();
    if (!list.length) return 0;
    return Math.round(list.reduce((s, g) => s + g.percentage, 0) / list.length);
  });

  readonly form = this.fb.nonNullable.group({
    studentId: [null as number | null, Validators.required],
    subjectId: [null as number | null, Validators.required],
    academicYearId: [null as number | null, Validators.required],
    gradeType: ['Assignment', Validators.required],
    score: [null as number | null, [Validators.required, Validators.min(0)]],
    maxScore: [100, [Validators.required, Validators.min(1)]],
    remarks: [''],
  });

  ngOnInit(): void {
    if (this.isStaff()) {
      this.userService.listStudents().subscribe((s) => this.students.set(s));
      this.academicService.listSubjects().subscribe((s) => this.subjects.set(s));
      this.academicService.listYears().subscribe((y) => this.years.set(y));
    } else if (this.isParent()) {
      const userId = this.auth.currentUser()?.id;
      this.userService.listParents().subscribe((parents) => {
        const me = parents.find((p) => p.user.id === userId);
        this.children.set(me?.children ?? []);
      });
    } else {
      this.gradeService.myGrades().subscribe((g) => this.grades.set(g));
    }
  }

  get viewStudentIdModel(): number | null {
    return this.viewStudentId();
  }
  set viewStudentIdModel(value: number | null) {
    this.viewStudentId.set(value);
    if (value) {
      this.gradeService.studentGrades(value).subscribe((g) => this.grades.set(g));
    }
  }

  record(): void {
    if (this.form.invalid) return this.form.markAllAsTouched();
    this.gradeService.record(this.form.getRawValue()).subscribe(() => {
      this.snackBar.open('Grade recorded', 'OK', { duration: 3000 });
      const studentId = this.form.controls.studentId.value;
      this.form.patchValue({ score: null, remarks: '' });
      if (studentId && studentId === this.viewStudentId()) {
        this.gradeService.studentGrades(studentId).subscribe((g) => this.grades.set(g));
      }
    });
  }

  viewChild(studentId: number): void {
    this.gradeService.studentGrades(studentId).subscribe((g) => this.grades.set(g));
  }

  deleteGrade(id: number): void {
    this.gradeService.delete(id).subscribe(() => {
      this.snackBar.open('Grade deleted', 'OK', { duration: 2500 });
      this.grades.set(this.grades().filter((g) => g.id !== id));
    });
  }
}
