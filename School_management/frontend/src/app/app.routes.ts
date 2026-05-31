import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then((m) => m.LoginComponent),
  },
  {
    path: '',
    loadComponent: () =>
      import('./layout/main-layout/main-layout').then((m) => m.MainLayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard').then((m) => m.DashboardComponent),
      },
      {
        path: 'announcements',
        loadComponent: () =>
          import('./features/announcements/announcements').then((m) => m.AnnouncementsComponent),
      },
      {
        path: 'profile',
        loadComponent: () => import('./features/profile/profile').then((m) => m.ProfileComponent),
      },
      {
        path: 'users',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN'] },
        loadComponent: () => import('./features/users/users').then((m) => m.UsersComponent),
      },
      {
        path: 'academic',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN'] },
        loadComponent: () =>
          import('./features/academic/academic').then((m) => m.AcademicComponent),
      },
      {
        path: 'attendance',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
        loadComponent: () =>
          import('./features/attendance/attendance').then((m) => m.AttendanceComponent),
      },
      {
        path: 'grades',
        canActivate: [roleGuard],
        data: { roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
        loadComponent: () => import('./features/grades/grades').then((m) => m.GradesComponent),
      },
      {
        path: 'timetable',
        loadComponent: () =>
          import('./features/timetable/timetable').then((m) => m.TimetableComponent),
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
