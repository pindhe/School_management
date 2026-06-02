import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { UserService } from '../../core/services/user.service';
import { AppUserSummary } from '../../core/models/user.model';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss',
})
export class UsersComponent implements OnInit {
  private readonly userService = inject(UserService);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly users = signal<AppUserSummary[]>([]);

  readonly adminCount = signal(0);
  readonly guestCount = signal(0);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.userService.list().subscribe({
      next: (data) => {
        this.users.set(data);
        this.adminCount.set(data.filter((u) => u.role === 'ADMIN').length);
        this.guestCount.set(data.filter((u) => u.role === 'GUEST').length);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load users from database. Admin access required.');
        this.loading.set(false);
      },
    });
  }
}
