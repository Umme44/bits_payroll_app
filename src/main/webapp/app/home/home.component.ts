import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { FormBuilder } from '@angular/forms';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { StateStorageService } from '../core/auth/state-storage.service';
import { LoginService } from '../login/login.service';
import { Authority } from '../config/authority.constants';
import Swal from 'sweetalert2';
import 'app/home/login-animation.js';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  authenticationError = false;

  username!: string;
  password!: string;
  rememberMe!: boolean;
  // rememberMe = true;
  showInfoMsg = '';
  anyAtSing = false;

  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private stateStorageService: StateStorageService,
    private fb: FormBuilder,
    private loginService: LoginService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        if (account != null) {
          if (this.accountService.hasAnyAuthority([Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER]))
            this.router.navigate(['dashboard']);
        }
      });
  }

  ngAfterViewInit(): void {
    (window as any).initialize();
  }

  login(): void {
    this.loginService
      .login({
        username: this.username,
        password: this.password,
        rememberMe: this.rememberMe,
      })
      .subscribe({
        next: v => {
          this.authenticationError = false;
          if (
            this.router.url === '/account/register' ||
            this.router.url.startsWith('/account/activate') ||
            this.router.url.startsWith('/account/reset/')
          ) {
            this.router.navigate(['']);
          }
        },
        error: (e: HttpErrorResponse) => {
          this.authenticationError = true;
          if(e.status===401){
            this.showWaitTimePopUp(e.error.detail)
          }
          else this.showLoginFailedPopup();
        },
      });
  }

  showWaitTimePopUp(message: string){
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: message,
      showConfirmButton: false,
      footer: '<a>Please contact helpdesk@bracits.com for any queries</a>',
      timer: 2000,
    });
  }

  showLoginFailedPopup(): void {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'Looks like you entered wrong LAN ID or passwords',
      showConfirmButton: false,
      footer: '<a>Please contact helpdesk@bracits.com for any queries</a>',
      timer: 2000,
    });
  }

  showInfo(): void {
    this.showInfoMsg = 'Please use first part of your email before @ as username';
  }

  hideInfo(): void {
    this.showInfoMsg = '';
  }

  checkAnyAtSign(): void {
    if (this.username.includes('@')) this.anyAtSing = true;
    else this.anyAtSing = false;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
