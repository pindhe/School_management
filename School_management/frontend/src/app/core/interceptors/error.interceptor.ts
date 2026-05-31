import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);
  const auth = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message = 'An unexpected error occurred';

      if (error.error && typeof error.error === 'object' && error.error.message) {
        message = error.error.message;
      } else if (error.status === 0) {
        message = 'Cannot reach the server. Is the backend running?';
      }

      if (error.status === 401 && !req.url.includes('/auth/login')) {
        message = 'Your session has expired. Please sign in again.';
        auth.logout();
      }

      // Don't surface a snackbar for the login screen's own validation; let it handle inline.
      if (!req.url.includes('/auth/login')) {
        snackBar.open(message, 'Dismiss', { duration: 5000, panelClass: 'sms-snackbar-error' });
      }

      return throwError(() => error);
    }),
  );
};
