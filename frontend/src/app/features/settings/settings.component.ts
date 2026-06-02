import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SettingsService } from '../../core/services/settings.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [FormsModule, DatePipe],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss',
})
export class SettingsComponent implements OnInit {
  private readonly settingsService = inject(SettingsService);

  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly error = signal<string | null>(null);
  readonly saved = signal(false);

  compactTables = false;
  emailNotifications = true;
  lastUpdated: string | null = null;

  ngOnInit(): void {
    this.settingsService.load().subscribe({
      next: (s) => {
        this.compactTables = s.compactTables;
        this.emailNotifications = s.emailNotifications;
        this.lastUpdated = s.updatedAt ?? null;
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load settings from database.');
        this.loading.set(false);
      },
    });
  }

  save(): void {
    this.saving.set(true);
    this.error.set(null);
    this.settingsService
      .save({
        compactTables: this.compactTables,
        emailNotifications: this.emailNotifications,
      })
      .subscribe({
        next: (s) => {
          this.lastUpdated = s.updatedAt ?? null;
          this.saved.set(true);
          this.saving.set(false);
          setTimeout(() => this.saved.set(false), 2500);
        },
        error: () => {
          this.error.set('Failed to save settings.');
          this.saving.set(false);
        },
      });
  }
}
