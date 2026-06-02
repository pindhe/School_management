import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { StudentService } from '../../../core/services/student.service';

@Component({
  selector: 'app-student-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './student-form.component.html',
  styleUrl: './student-form.component.scss',
})
export class StudentFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly studentService = inject(StudentService);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly previewUrl = signal<string | null>(null);
  private photoFile: File | null = null;
  private studentId: number | null = null;

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(120)]],
    address: ['', [Validators.required, Validators.maxLength(255)]],
    phone: ['', [Validators.required, Validators.maxLength(30)]],
    email: ['', [Validators.required, Validators.email]],
  });

  get isEdit(): boolean {
    return this.studentId !== null;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.studentId = Number(idParam);
      this.studentService.get(this.studentId).subscribe({
        next: (student) => {
          this.form.patchValue({
            name: student.name,
            address: student.address,
            phone: student.phone,
            email: student.email,
          });
          this.previewUrl.set(this.studentService.photoUrl(student.photoUrl));
        },
        error: () => this.error.set('Student not found.'),
      });
    }
  }

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }
    this.photoFile = file;
    const reader = new FileReader();
    reader.onload = () => this.previewUrl.set(reader.result as string);
    reader.readAsDataURL(file);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading.set(true);
    this.error.set(null);
    const request = this.form.getRawValue();
    const action = this.isEdit
      ? this.studentService.update(this.studentId!, request, this.photoFile)
      : this.studentService.create(request, this.photoFile);

    action.subscribe({
      next: () => this.router.navigate(['/students']),
      error: (err) => {
        this.error.set(err?.error?.detail ?? 'Could not save student.');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }
}
