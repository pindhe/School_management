import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTabsModule } from '@angular/material/tabs';
import { SchoolClass, ParentResponse, StudentResponse, TeacherResponse } from '../../core/models/models';
import { AcademicService } from '../../core/services/academic.service';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-users',
  imports: [
    ReactiveFormsModule,
    MatTabsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './users.html',
})
export class UsersComponent implements OnInit {
  private userService = inject(UserService);
  private academicService = inject(AcademicService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  readonly teachers = signal<TeacherResponse[]>([]);
  readonly students = signal<StudentResponse[]>([]);
  readonly parents = signal<ParentResponse[]>([]);
  readonly classes = signal<SchoolClass[]>([]);

  readonly showTeacherForm = signal(false);
  readonly showStudentForm = signal(false);
  readonly showParentForm = signal(false);

  readonly teacherForm = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: [''],
    employeeId: ['', Validators.required],
    department: [''],
    phoneNumber: [''],
  });

  readonly studentForm = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: [''],
    admissionNumber: ['', Validators.required],
    currentClassId: [null as number | null],
    phoneNumber: [''],
  });

  readonly parentForm = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: [''],
    phoneNumber: [''],
  });

  readonly linkForm = this.fb.nonNullable.group({
    parentId: [null as number | null, Validators.required],
    studentId: [null as number | null, Validators.required],
    relationship: ['GUARDIAN'],
  });

  readonly enrollForm = this.fb.nonNullable.group({
    studentId: [null as number | null, Validators.required],
    classId: [null as number | null, Validators.required],
  });

  ngOnInit(): void {
    this.loadAll();
  }

  private loadAll(): void {
    this.userService.listTeachers().subscribe((t) => this.teachers.set(t));
    this.userService.listStudents().subscribe((s) => this.students.set(s));
    this.userService.listParents().subscribe((p) => this.parents.set(p));
    this.academicService.listClasses().subscribe((c) => this.classes.set(c));
  }

  createTeacher(): void {
    if (this.teacherForm.invalid) return this.teacherForm.markAllAsTouched();
    this.userService.createTeacher(this.cleanPayload(this.teacherForm.getRawValue())).subscribe(() => {
      this.snackBar.open('Teacher created', 'OK', { duration: 3000 });
      this.teacherForm.reset();
      this.showTeacherForm.set(false);
      this.userService.listTeachers().subscribe((t) => this.teachers.set(t));
    });
  }

  createStudent(): void {
    if (this.studentForm.invalid) return this.studentForm.markAllAsTouched();
    this.userService.createStudent(this.cleanPayload(this.studentForm.getRawValue())).subscribe(() => {
      this.snackBar.open('Student created', 'OK', { duration: 3000 });
      this.studentForm.reset();
      this.showStudentForm.set(false);
      this.userService.listStudents().subscribe((s) => this.students.set(s));
    });
  }

  createParent(): void {
    if (this.parentForm.invalid) return this.parentForm.markAllAsTouched();
    this.userService.createParent(this.cleanPayload(this.parentForm.getRawValue())).subscribe(() => {
      this.snackBar.open('Parent created', 'OK', { duration: 3000 });
      this.parentForm.reset();
      this.showParentForm.set(false);
      this.userService.listParents().subscribe((p) => this.parents.set(p));
    });
  }

  linkParent(): void {
    if (this.linkForm.invalid) return this.linkForm.markAllAsTouched();
    const v = this.linkForm.getRawValue();
    this.userService
      .linkParent({ parentId: v.parentId!, studentId: v.studentId!, relationship: v.relationship })
      .subscribe(() => {
        this.snackBar.open('Parent linked to student', 'OK', { duration: 3000 });
        this.linkForm.reset({ relationship: 'GUARDIAN' });
        this.userService.listParents().subscribe((p) => this.parents.set(p));
      });
  }

  enroll(): void {
    if (this.enrollForm.invalid) return this.enrollForm.markAllAsTouched();
    const v = this.enrollForm.getRawValue();
    this.academicService.enroll(v.studentId!, v.classId!).subscribe(() => {
      this.snackBar.open('Student enrolled', 'OK', { duration: 3000 });
      this.enrollForm.reset();
      this.userService.listStudents().subscribe((s) => this.students.set(s));
    });
  }

  toggleActive(userId: number, current: boolean): void {
    this.userService.setActive(userId, !current).subscribe(() => {
      this.snackBar.open('Status updated', 'OK', { duration: 2500 });
      this.loadAll();
    });
  }

  private cleanPayload<T extends Record<string, unknown>>(value: T): Partial<T> {
    const result: Record<string, unknown> = {};
    for (const [key, val] of Object.entries(value)) {
      if (val !== '' && val !== null && val !== undefined) {
        result[key] = val;
      }
    }
    return result as Partial<T>;
  }
}
