import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { RoleName } from '../models/models';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const allowed = (route.data?.['roles'] as RoleName[] | undefined) ?? [];
  if (!auth.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }
  if (allowed.length === 0 || auth.hasRole(...allowed)) {
    return true;
  }
  return router.createUrlTree(['/dashboard']);
};
