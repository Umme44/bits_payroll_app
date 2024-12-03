import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UserFeedbackFormService, UserFeedbackFormGroup } from './user-feedback-form.service';
import { IUserFeedback } from '../user-feedback.model';
import { UserFeedbackService } from '../service/user-feedback.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-user-feedback-update',
  templateUrl: './user-feedback-update.component.html',
})
export class UserFeedbackUpdateComponent implements OnInit {
  isSaving = false;
  userFeedback: IUserFeedback | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: UserFeedbackFormGroup = this.userFeedbackFormService.createUserFeedbackFormGroup();

  constructor(
    protected userFeedbackService: UserFeedbackService,
    protected userFeedbackFormService: UserFeedbackFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userFeedback }) => {
      this.userFeedback = userFeedback;
      if (userFeedback) {
        this.updateForm(userFeedback);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userFeedback = this.userFeedbackFormService.getUserFeedback(this.editForm);
    if (userFeedback.id !== null) {
      this.subscribeToSaveResponse(this.userFeedbackService.update(userFeedback));
    } else {
      this.subscribeToSaveResponse(this.userFeedbackService.create(userFeedback as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserFeedback>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userFeedback: IUserFeedback): void {
    this.userFeedback = userFeedback;
    this.userFeedbackFormService.resetForm(this.editForm, userFeedback);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userFeedback.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userFeedback?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
