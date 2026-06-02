import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UserSettings } from '../models/settings.model';

@Injectable({ providedIn: 'root' })
export class SettingsService {
  private readonly api = `${environment.apiUrl}/settings`;

  readonly current = signal<UserSettings | null>(null);

  constructor(private readonly http: HttpClient) {}

  load(): Observable<UserSettings> {
    return this.http.get<UserSettings>(`${this.api}/me`).pipe(
      tap((s) => this.current.set(s)),
    );
  }

  save(settings: UserSettings): Observable<UserSettings> {
    return this.http.put<UserSettings>(`${this.api}/me`, settings).pipe(
      tap((s) => this.current.set(s)),
    );
  }
}
