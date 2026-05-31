import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';
import {
  AcademicYear,
  SchoolClass,
  Subject,
  TeacherAssignment,
  TeacherResponse,
} from '../../core/models/models';
import { AcademicService } from '../../core/services/academic.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-academic',
  imports: [
    ReactiveFormsModule,
    MatTabsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
  ],
  templateUrl: './academic.html',
})
export class AcademicComponent implements OnInit {
  private academicService = inject(AcademicService);
  private userService = inject(UserService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  readonly years = signal<AcademicYear[]>([]);
  readonly subjects = signal<Subject[]>([]);
  readonly classes = signal<SchoolClass[]>([]);
  readonly teachers = signal<TeacherResponse[]>([]);
  readonly assignments = signal<TeacherAssignment[]>([]);

  readonly yearForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    yearStart: [new Date().getFullYear(), Validators.required],
    yearEnd: [new Date().getFullYear() + 1, Validators.required],
    active: [false],
  });

  readonly subjectForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    code: ['', Validators.required],
    description: [''],
  });

  readonly classForm = this.fb.nonNullable.group({
    name: ['', Validators.required],
    academicYearId: [null as number | null, Validators.required],
    homeroomTeacherId: [null as number | null],
  });

  readonly assignSubjectsForm = this.fb.nonNullable.group({
    classId: [null as number | null, Validators.required],
    subjectIds: [[] as number[], Validators.required],
  });

  readonly assignmentForm = this.fb.nonNullable.group({
    teacherId: [null as number | null, Validators.required],
    classId: [null as number | null, Validators.required],
    subjectId: [null as number | null, Validators.required],
    academicYearId: [null as number | null, Validators.required],
  });

  ngOnInit(): void {
    this.reload();
  }

  private reload(): void {
    this.academicService.listYears().subscribe((y) => this.years.set(y));
    this.academicService.listSubjects().subscribe((s) => this.subjects.set(s));
    this.academicService.listClasses().subscribe((c) => this.classes.set(c));
    this.userService.listTeachers().subscribe((t) => this.teachers.set(t));
    this.academicService.listAssignments().subscribe((a) => this.assignments.set(a));
  }

  createYear(): void {
    if (this.yearForm.invalid) return this.yearForm.markAllAsTouched();
    this.academicService.createYear(this.yearForm.getRawValue()).subscribe(() => {
      this.snackBar.open('Academic year created', 'OK', { duration: 3000 });
      this.yearForm.reset({ name: '', yearStart: new Date().getFullYear(), yearEnd: new Date().getFullYear() + 1, active: false });
      this.academicService.listYears().subscribe((y) => this.years.set(y));
    });
  }

  createSubject(): void {
    if (this.subjectForm.invalid) return this.subjectForm.markAllAsTouched();
    this.academicService.createSubject(this.subjectForm.getRawValue()).subscribe(() => {
      this.snackBar.open('Subject created', 'OK', { duration: 3000 });
      this.subjectForm.reset();
      this.academicService.listSubjects().subscribe((s) => this.subjects.set(s));
    });
  }

  createClass(): void {
    if (this.classForm.invalid) return this.classForm.markAllAsTouched();
    const v = this.classForm.getRawValue();
    const payload: Record<string, unknown> = { name: v.name, academicYearId: v.academicYearId };
    if (v.homeroomTeacherId) payload['homeroomTeacherId'] = v.homeroomTeacherId;
    this.academicService.createClass(payload).subscribe(() => {
      this.snackBar.open('Class created', 'OK', { duration: 3000 });
      this.classForm.reset();
      this.academicService.listClasses().subscribe((c) => this.classes.set(c));
    });
  }

  assignSubjects(): void {
    if (this.assignSubjectsForm.invalid) return this.assignSubjectsForm.markAllAsTouched();
    const v = this.assignSubjectsForm.getRawValue();
    this.academicService.assignSubjects(v.classId!, v.subjectIds).subscribe(() => {
      this.snackBar.open('Subjects assigned to class', 'OK', { duration: 3000 });
      this.assignSubjectsForm.reset({ classId: null, subjectIds: [] });
      this.academicService.listClasses().subscribe((c) => this.classes.set(c));
    });
  }

  createAssignment(): void {
    if (this.assignmentForm.invalid) return this.assignmentForm.markAllAsTouched();
    this.academicService.assignTeacher(this.assignmentForm.getRawValue()).subscribe(() => {
      this.snackBar.open('Teacher assigned', 'OK', { duration: 3000 });
      this.assignmentForm.reset();
      this.academicService.listAssignments().subscribe((a) => this.assignments.set(a));
    });
  }
}
