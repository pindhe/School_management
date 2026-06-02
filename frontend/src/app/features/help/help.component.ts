import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { SystemService } from '../../core/services/system.service';
import { SystemInfo } from '../../core/models/system.model';

@Component({
  selector: 'app-help',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './help.component.html',
  styleUrl: './help.component.scss',
})
export class HelpComponent implements OnInit {
  private readonly systemService = inject(SystemService);

  readonly loading = signal(true);
  readonly info = signal<SystemInfo | null>(null);

  ngOnInit(): void {
    this.systemService.getInfo().subscribe({
      next: (data) => {
        this.info.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }
}
