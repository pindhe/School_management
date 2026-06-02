import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { StudentService } from '../../core/services/student.service';
import { AuthService } from '../../core/services/auth.service';
import { Student } from '../../core/models/student.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  private readonly studentService = inject(StudentService);
  readonly auth = inject(AuthService);

  readonly loading = signal(true);
  readonly students = signal<Student[]>([]);

  readonly totalStudents = signal(0);
  readonly withPhoto = signal(0);
  readonly uniqueDomains = signal(0);

  ngOnInit(): void {
    this.studentService.list().subscribe({
      next: (data) => {
        this.students.set(data);
        this.totalStudents.set(data.length);
        this.withPhoto.set(data.filter((s) => !!s.photoUrl).length);
        const domains = new Set(data.map((s) => s.email.split('@')[1]).filter(Boolean));
        this.uniqueDomains.set(domains.size);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  recentStudents(): Student[] {
    return this.students().slice(0, 5);
  }
}
