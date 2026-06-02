import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Student, StudentRequest } from '../models/student.model';

@Injectable({ providedIn: 'root' })
export class StudentService {
  private readonly api = `${environment.apiUrl}/students`;

  constructor(private readonly http: HttpClient) {}

  list(query?: string): Observable<Student[]> {
    const params = query?.trim() ? { q: query.trim() } : undefined;
    return this.http.get<Student[]>(this.api, { params });
  }

  get(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.api}/${id}`);
  }

  create(request: StudentRequest, photo?: File | null): Observable<Student> {
    return this.http.post<Student>(this.api, this.toFormData(request, photo));
  }

  update(id: number, request: StudentRequest, photo?: File | null): Observable<Student> {
    return this.http.put<Student>(`${this.api}/${id}`, this.toFormData(request, photo));
  }

  delete(id: number): Observable<{ message: string }> {
    return this.http.delete<{ message: string }>(`${this.api}/${id}`);
  }

  photoUrl(path?: string | null): string | null {
    if (!path) {
      return null;
    }
    if (path.startsWith('http')) {
      return path;
    }
    return path.startsWith('/') ? path : `/${path}`;
  }

  private toFormData(request: StudentRequest, photo?: File | null): FormData {
    const form = new FormData();
    form.append(
      'student',
      new Blob([JSON.stringify(request)], { type: 'application/json' }),
    );
    if (photo) {
      form.append('photo', photo);
    }
    return form;
  }
}
