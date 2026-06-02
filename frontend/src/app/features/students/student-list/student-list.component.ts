import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { StudentService } from '../../../core/services/student.service';
import { AuthService } from '../../../core/services/auth.service';
import { Student } from '../../../core/models/student.model';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.scss',
})
export class StudentListComponent implements OnInit {
  private readonly studentService = inject(StudentService);
  readonly auth = inject(AuthService);

  readonly students = signal<Student[]>([]);
  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly searchControl = new FormControl('', { nonNullable: true });

  ngOnInit(): void {
    this.load();
    this.searchControl.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((q) => this.load(q));
  }

  load(query = ''): void {
    this.loading.set(true);
    this.error.set(null);
    this.studentService.list(query).subscribe({
      next: (data) => {
        this.students.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load students.');
        this.loading.set(false);
      },
    });
  }

  photoUrl(student: Student): string | null {
    return this.studentService.photoUrl(student.photoUrl);
  }

  deleteStudent(student: Student): void {
    if (!confirm(`Delete ${student.name}?`)) {
      return;
    }
    this.studentService.delete(student.id).subscribe({
      next: () => this.load(this.searchControl.value),
      error: () => alert('Delete failed. Admin role required.'),
    });
  }
}
