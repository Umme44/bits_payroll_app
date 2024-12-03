import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserFeedback } from '../user-feedback.model';

@Component({
  selector: 'jhi-user-feedback-detail',
  templateUrl: './user-feedback-detail.component.html',
})
export class UserFeedbackDetailComponent implements OnInit {
  userFeedback: IUserFeedback | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userFeedback }) => {
      this.userFeedback = userFeedback;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
