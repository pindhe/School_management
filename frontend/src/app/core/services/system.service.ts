import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { SystemInfo } from '../models/system.model';

@Injectable({ providedIn: 'root' })
export class SystemService {
  private readonly api = `${environment.apiUrl}/system`;

  constructor(private readonly http: HttpClient) {}

  getInfo(): Observable<SystemInfo> {
    return this.http.get<SystemInfo>(`${this.api}/info`);
  }
}
