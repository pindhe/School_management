import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { NavItem } from '../../core/models/nav.model';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.scss',
})
export class MainLayoutComponent {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly sidebarOpen = signal(false);

  private readonly allNavItems: NavItem[] = [
    { label: 'Dashboard', icon: 'bi-speedometer2', route: '/dashboard', exact: true },
    { label: 'Students', icon: 'bi-people', route: '/students', exact: true },
    { label: 'Add Student', icon: 'bi-person-plus', route: '/students/new', adminOnly: true },
    { label: 'Reports', icon: 'bi-bar-chart-line', route: '/reports', adminOnly: true },
    { label: 'Users', icon: 'bi-shield-lock', route: '/users', adminOnly: true },
    { label: 'Profile', icon: 'bi-person-badge', route: '/profile', exact: true },
    { label: 'Settings', icon: 'bi-gear', route: '/settings', exact: true },
    { label: 'Help', icon: 'bi-question-circle', route: '/help', exact: true },
  ];

  readonly navItems = computed(() =>
    this.allNavItems.filter((item) => !item.adminOnly || this.auth.isAdmin()),
  );

  readonly pageTitle = computed(() => {
    const url = this.router.url.split('?')[0];
    if (url.includes('/students/new')) {
      return 'Add Student';
    }
    if (url.includes('/edit')) {
      return 'Edit Student';
    }
    const item = this.allNavItems.find((n) =>
      n.exact ? url === n.route : url.startsWith(n.route),
    );
    return item?.label ?? 'Student Registration';
  });

  toggleSidebar(): void {
    this.sidebarOpen.update((v) => !v);
  }

  closeSidebarOnMobile(): void {
    this.sidebarOpen.set(false);
  }
}
