import { Component, OnInit, inject, signal } from '@angular/core';
import { ReportService } from '../../core/services/report.service';
import { ReportSummary } from '../../core/models/report.model';

@Component({
  selector: 'app-reports',
  standalone: true,
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.scss',
})
export class ReportsComponent implements OnInit {
  private readonly reportService = inject(ReportService);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly report = signal<ReportSummary | null>(null);

  ngOnInit(): void {
    this.reportService.getSummary().subscribe({
      next: (data) => {
        this.report.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load reports. Admin access required.');
        this.loading.set(false);
      },
    });
  }

  maxDomainCount(): number {
    const domains = this.report()?.studentsByEmailDomain ?? [];
    return domains.length ? Math.max(...domains.map((d) => d.count)) : 1;
  }
}
