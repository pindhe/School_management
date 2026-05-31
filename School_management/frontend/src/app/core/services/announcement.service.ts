import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Announcement, MessageResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class AnnouncementService {
  private readonly baseUrl = `${environment.apiUrl}/announcements`;

  constructor(private http: HttpClient) {}

  feed(): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(`${this.baseUrl}/feed`);
  }

  listAll(): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(this.baseUrl);
  }

  create(payload: unknown): Observable<Announcement> {
    return this.http.post<Announcement>(this.baseUrl, payload);
  }

  deactivate(id: number): Observable<MessageResponse> {
    return this.http.delete<MessageResponse>(`${this.baseUrl}/${id}`);
  }
}
