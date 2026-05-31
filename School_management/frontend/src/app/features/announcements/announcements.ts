import { DatePipe } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Announcement, SchoolClass, TargetRole } from '../../core/models/models';
import { AcademicService } from '../../core/services/academic.service';
import { AnnouncementService } from '../../core/services/announcement.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-announcements',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './announcements.html',
})
export class AnnouncementsComponent implements OnInit {
  private auth = inject(AuthService);
  private service = inject(AnnouncementService);
  private academicService = inject(AcademicService);
  private fb = inject(FormBuilder);
  private snackBar = inject(MatSnackBar);

  readonly announcements = signal<Announcement[]>([]);
  readonly classes = signal<SchoolClass[]>([]);
  readonly showForm = signal(false);
  readonly targetRoles: TargetRole[] = ['ALL', 'ADMIN', 'TEACHER', 'STUDENT', 'PARENT'];

  readonly isAdmin = computed(() => this.auth.hasRole('ADMIN'));
  readonly canPublish = computed(() => this.auth.hasRole('ADMIN', 'TEACHER'));

  readonly form = this.fb.nonNullable.group({
    title: ['', Validators.required],
    content: ['', Validators.required],
    targetRole: ['ALL' as TargetRole, Validators.required],
    targetClassId: [null as number | null],
  });

  ngOnInit(): void {
    this.load();
    if (this.canPublish()) {
      this.academicService.listClasses().subscribe((c) => this.classes.set(c));
    }
  }

  private load(): void {
    const source = this.isAdmin() ? this.service.listAll() : this.service.feed();
    source.subscribe((list) => this.announcements.set(list));
  }

  toggleForm(): void {
    this.showForm.set(!this.showForm());
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.service.create(this.form.getRawValue()).subscribe(() => {
      this.snackBar.open('Announcement published', 'OK', { duration: 3000 });
      this.form.reset({ targetRole: 'ALL', targetClassId: null, title: '', content: '' });
      this.showForm.set(false);
      this.load();
    });
  }

  remove(item: Announcement): void {
    this.service.deactivate(item.id).subscribe(() => {
      this.snackBar.open('Announcement removed', 'OK', { duration: 3000 });
      this.load();
    });
  }
}
