import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { DashboardService } from '../../core/services/dashboard.service';
import { StudentService } from '../../core/services/student.service';
import { AuthService } from '../../core/services/auth.service';
import { DashboardStats } from '../../core/models/dashboard.model';
import { Student } from '../../core/models/student.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, DatePipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);
  private readonly studentService = inject(StudentService);
  readonly auth = inject(AuthService);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly stats = signal<DashboardStats | null>(null);

  ngOnInit(): void {
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.loading.set(false);
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 401) {
          this.error.set('Session expired. Please log out and sign in again.');
        } else if (err.status === 0) {
          this.error.set('Cannot reach API. Start the backend: cd backend && .\\mvnw.cmd spring-boot:run');
        } else {
          this.error.set(err.error?.detail ?? 'Could not load dashboard data from server.');
        }
        this.loading.set(false);
      },
    });
  }

  photoUrl(student: Student): string | null {
    return this.studentService.photoUrl(student.photoUrl);
  }
}
