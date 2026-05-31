import { Component, computed, inject, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { RoleName } from '../../core/models/models';
import { AuthService } from '../../core/services/auth.service';

interface NavItem {
  label: string;
  icon: string;
  path: string;
  roles: RoleName[];
}

@Component({
  selector: 'app-main-layout',
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatDividerModule,
  ],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss',
})
export class MainLayoutComponent {
  private auth = inject(AuthService);

  readonly user = this.auth.currentUser;
  readonly sidenavOpen = signal(true);

  private readonly allNavItems: NavItem[] = [
    { label: 'Dashboard', icon: 'dashboard', path: '/dashboard', roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
    { label: 'User Management', icon: 'group', path: '/users', roles: ['ADMIN'] },
    { label: 'Academic Setup', icon: 'menu_book', path: '/academic', roles: ['ADMIN'] },
    { label: 'Attendance', icon: 'fact_check', path: '/attendance', roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
    { label: 'Grades', icon: 'grade', path: '/grades', roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
    { label: 'Timetable', icon: 'schedule', path: '/timetable', roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
    { label: 'Announcements', icon: 'campaign', path: '/announcements', roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
    { label: 'My Profile', icon: 'account_circle', path: '/profile', roles: ['ADMIN', 'TEACHER', 'STUDENT', 'PARENT'] },
  ];

  readonly navItems = computed<NavItem[]>(() => {
    const role = this.auth.role();
    if (!role) {
      return [];
    }
    return this.allNavItems.filter((item) => item.roles.includes(role));
  });

  readonly roleLabel = computed(() => {
    const role = this.auth.role();
    if (!role) return '';
    return role.charAt(0) + role.slice(1).toLowerCase();
  });

  readonly initials = computed(() => {
    const u = this.user();
    if (!u) return '';
    return `${u.firstName?.[0] ?? ''}${u.lastName?.[0] ?? ''}`.toUpperCase();
  });

  toggleSidenav(): void {
    this.sidenavOpen.set(!this.sidenavOpen());
  }

  logout(): void {
    this.auth.logout();
  }
}
