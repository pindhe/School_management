import { Component, inject, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Announcement } from '../../core/models/models';
import { AnnouncementService } from '../../core/services/announcement.service';
import { AcademicService } from '../../core/services/academic.service';
import { UserService } from '../../core/services/user.service';
import { GradeService } from '../../core/services/grade.service';
import { AttendanceService } from '../../core/services/attendance.service';
import { AuthService } from '../../core/services/auth.service';

interface StatCard {
  label: string;
  value: string | number;
  icon: string;
  color: string;
}

@Component({
  selector: 'app-dashboard',
  imports: [DatePipe, RouterLink, MatCardModule, MatIconModule, MatButtonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class DashboardComponent implements OnInit {
  private auth = inject(AuthService);
  private announcementService = inject(AnnouncementService);
  private academicService = inject(AcademicService);
  private userService = inject(UserService);
  private gradeService = inject(GradeService);
  private attendanceService = inject(AttendanceService);

  readonly user = this.auth.currentUser;
  readonly stats = signal<StatCard[]>([]);
  readonly announcements = signal<Announcement[]>([]);
  readonly loading = signal(true);

  ngOnInit(): void {
    this.loadAnnouncements();
    this.loadStats();
  }

  greeting(): string {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  }

  private loadAnnouncements(): void {
    this.announcementService
      .feed()
      .pipe(catchError(() => of([] as Announcement[])))
      .subscribe((list) => this.announcements.set(list.slice(0, 5)));
  }

  private loadStats(): void {
    const role = this.auth.role();
    this.loading.set(true);

    if (role === 'ADMIN') {
      forkJoin({
        teachers: this.userService.listTeachers().pipe(catchError(() => of([]))),
        students: this.userService.listStudents().pipe(catchError(() => of([]))),
        classes: this.academicService.listClasses().pipe(catchError(() => of([]))),
        subjects: this.academicService.listSubjects().pipe(catchError(() => of([]))),
      }).subscribe((res) => {
        this.stats.set([
          { label: 'Teachers', value: res.teachers.length, icon: 'co_present', color: '#1565c0' },
          { label: 'Students', value: res.students.length, icon: 'school', color: '#2e7d32' },
          { label: 'Classes', value: res.classes.length, icon: 'meeting_room', color: '#ef6c00' },
          { label: 'Subjects', value: res.subjects.length, icon: 'menu_book', color: '#6a1b9a' },
        ]);
        this.loading.set(false);
      });
    } else if (role === 'STUDENT') {
      forkJoin({
        grades: this.gradeService.myGrades().pipe(catchError(() => of([]))),
        attendance: this.attendanceService.myAttendance().pipe(catchError(() => of([]))),
      }).subscribe((res) => {
        const present = res.attendance.filter((a) => a.status === 'PRESENT').length;
        const rate = res.attendance.length
          ? Math.round((present / res.attendance.length) * 100)
          : 0;
        const avg = res.grades.length
          ? Math.round(res.grades.reduce((s, g) => s + g.percentage, 0) / res.grades.length)
          : 0;
        this.stats.set([
          { label: 'Recorded Grades', value: res.grades.length, icon: 'grade', color: '#1565c0' },
          { label: 'Average', value: `${avg}%`, icon: 'trending_up', color: '#2e7d32' },
          { label: 'Attendance', value: `${rate}%`, icon: 'fact_check', color: '#ef6c00' },
        ]);
        this.loading.set(false);
      });
    } else if (role === 'TEACHER') {
      this.stats.set([
        { label: 'Attendance', value: 'Mark', icon: 'fact_check', color: '#1565c0' },
        { label: 'Grades', value: 'Record', icon: 'grade', color: '#2e7d32' },
        { label: 'Timetable', value: 'View', icon: 'schedule', color: '#ef6c00' },
      ]);
      this.loading.set(false);
    } else {
      this.stats.set([]);
      this.loading.set(false);
    }
  }
}
