import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { timer } from 'rxjs';
import { takeWhile } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { UserFeedbackService } from '../../shared/legacy/legacy-service/user-feedback.service';
import { LoginService } from '../../login/login.service';
import { NavbarService } from '../../layouts/navbar/navbar.service';
import { IUserFeedback, UserFeedback } from '../../shared/legacy/legacy-model/user-feedback.model';
import { swalOnGenerateSuccess, swalOnRequestError } from '../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-user-feedback',
  templateUrl: './user-feedback-rating.component.html',
  styleUrls: ['./user-feedback-rating.component.scss'],
})
export class UserFeedbackRatingComponent implements OnInit, OnDestroy {
  selectedRating!: number;
  starValues = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1];
  takeSuggestion = false;
  countDownDisplay = '';
  alive = true;
  navBarStatus!: string;
  subscription!: Subscription;
  progressBarValue = 100;

  editForm = this.fb.group({
    id: [],
    rating: [null, [Validators.required]],
    suggestion: [null, [Validators.minLength(0), Validators.maxLength(255)]],
    userId: [],
  });

  constructor(
    private fb: FormBuilder,
    protected userFeedbackService: UserFeedbackService,
    protected loginService: LoginService,
    private router: Router,
    private navbarService: NavbarService
  ) {}

  ngOnInit(): void {
    this.countDown();
    this.subscription = this.navbarService.currentMessage.subscribe(message => (this.navBarStatus = message));
    this.newMessage();
  }

  ngOnDestroy(): void {
    this.alive = false;
    this.navbarService.changeMessage('show');
    this.subscription.unsubscribe();
  }

  newMessage(): void {
    this.navbarService.changeMessage('hide');
  }

  countDown(): void {
    const now = 0;
    let countDownTo = 60 * 2;
    const progressBarDecrease = 100 / countDownTo;
    timer(0, 1000)
      .pipe(takeWhile(() => this.alive))
      .subscribe(_ => {
        const distance = countDownTo - now;
        this.countDownDisplay = countDownTo + 's ';

        countDownTo--;
        this.progressBarValue -= progressBarDecrease;

        if (distance <= 0) {
          this.loginService.logout();
          this.router.navigate(['']);
        }
      });
  }

  onClickValue(value: any): void {
    this.selectedRating = value;
    if (this.selectedRating === 1) {
      this.takeSuggestion = true;
    } else {
      this.takeSuggestion = false;
    }
  }

  private createFromForm(): IUserFeedback {
    return {
      ...new UserFeedback(),
      id: this.editForm.get(['id'])!.value,
      rating: this.selectedRating,
      suggestion: this.editForm.get(['suggestion'])!.value,
    };
  }

  save(): void {
    const userFeedback = this.createFromForm();
    this.userFeedbackService.createCommon(userFeedback).subscribe(
      res => {
        swalOnGenerateSuccess('Thank you!');

        //delay 1500ms due to swal
        setTimeout(() => {
          this.loginService.logout();
          this.router.navigate(['']);
        }, 1500);
      },
      () => swalOnRequestError()
    );
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  back(): void {
    window.history.back();
  }
}
