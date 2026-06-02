import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

const STORAGE_KEY = 'srs_settings';

interface AppSettings {
  compactTables: boolean;
  emailNotifications: boolean;
}

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss',
})
export class SettingsComponent {
  readonly saved = signal(false);

  compactTables = false;
  emailNotifications = true;

  constructor() {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (raw) {
      try {
        const s = JSON.parse(raw) as AppSettings;
        this.compactTables = s.compactTables;
        this.emailNotifications = s.emailNotifications;
      } catch {
        /* ignore */
      }
    }
  }

  save(): void {
    const settings: AppSettings = {
      compactTables: this.compactTables,
      emailNotifications: this.emailNotifications,
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(settings));
    this.saved.set(true);
    setTimeout(() => this.saved.set(false), 2500);
  }
}
