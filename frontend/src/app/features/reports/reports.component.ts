import { Component, OnInit, inject, signal } from '@angular/core';
import { StudentService } from '../../core/services/student.service';
import { Student } from '../../core/models/student.model';

@Component({
  selector: 'app-reports',
  standalone: true,
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.scss',
})
export class ReportsComponent implements OnInit {
  private readonly studentService = inject(StudentService);

  readonly loading = signal(true);
  readonly students = signal<Student[]>([]);

  ngOnInit(): void {
    this.studentService.list().subscribe({
      next: (data) => {
        this.students.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  photoCoverage(): number {
    const list = this.students();
    if (list.length === 0) {
      return 0;
    }
    return Math.round((list.filter((s) => s.photoUrl).length / list.length) * 100);
  }

  topDomains(): { domain: string; count: number }[] {
    const counts = new Map<string, number>();
    for (const s of this.students()) {
      const domain = s.email.split('@')[1] ?? 'unknown';
      counts.set(domain, (counts.get(domain) ?? 0) + 1);
    }
    return [...counts.entries()]
      .map(([domain, count]) => ({ domain, count }))
      .sort((a, b) => b.count - a.count)
      .slice(0, 5);
  }

  maxDomainCount(): number {
    const tops = this.topDomains();
    return tops.length ? Math.max(...tops.map((d) => d.count)) : 1;
  }
}
