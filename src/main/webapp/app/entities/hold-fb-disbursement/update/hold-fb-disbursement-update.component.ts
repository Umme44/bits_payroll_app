import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {finalize, map} from 'rxjs/operators';

import {HoldFbDisbursementFormGroup, HoldFbDisbursementFormService} from './hold-fb-disbursement-form.service';
import {IHoldFbDisbursement} from '../hold-fb-disbursement.model';
import {HoldFbDisbursementService} from '../service/hold-fb-disbursement.service';
import {IUser} from 'app/entities/user/user.model';
import {UserService} from 'app/entities/user/user.service';
import {IFestivalBonusDetails} from 'app/entities/festival-bonus-details/festival-bonus-details.model';
import {FestivalBonusDetailsService} from 'app/entities/festival-bonus-details/service/festival-bonus-details.service';

type SelectableEntity = IUser | IFestivalBonusDetails;

@Component({
  selector: 'jhi-hold-fb-disbursement-update',
  templateUrl: './hold-fb-disbursement-update.component.html',
})
export class HoldFbDisbursementUpdateComponent implements OnInit {
  isSaving = false;
  holdFbDisbursement: IHoldFbDisbursement | null = null;

  usersSharedCollection: IUser[] = [];
  festivalBonusDetailsSharedCollection: IFestivalBonusDetails[] = [];

  editForm: HoldFbDisbursementFormGroup = this.holdFbDisbursementFormService.createHoldFbDisbursementFormGroup();

  constructor(
    protected holdFbDisbursementService: HoldFbDisbursementService,
    protected holdFbDisbursementFormService: HoldFbDisbursementFormService,
    protected userService: UserService,
    protected festivalBonusDetailsService: FestivalBonusDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {
  }

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareFestivalBonusDetails = (o1: IFestivalBonusDetails | null, o2: IFestivalBonusDetails | null): boolean =>
    this.festivalBonusDetailsService.compareFestivalBonusDetails(o1, o2);
  users: IUser[] = [];
  festivalbonusdetails: IFestivalBonusDetails[] = [];

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({holdFbDisbursement}) => {
      this.holdFbDisbursement = holdFbDisbursement;
      if (holdFbDisbursement) {
        this.updateForm(holdFbDisbursement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const holdFbDisbursement = this.holdFbDisbursementFormService.getHoldFbDisbursement(this.editForm);
    if (holdFbDisbursement.id !== null) {
      this.subscribeToSaveResponse(this.holdFbDisbursementService.update(holdFbDisbursement));
    } else {
      this.subscribeToSaveResponse(this.holdFbDisbursementService.create(holdFbDisbursement as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHoldFbDisbursement>>): void {
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

  protected updateForm(holdFbDisbursement: IHoldFbDisbursement): void {
    this.holdFbDisbursement = holdFbDisbursement;
    this.holdFbDisbursementFormService.resetForm(this.editForm, holdFbDisbursement);

    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   holdFbDisbursement.disbursedBy
    // );
    // this.festivalBonusDetailsSharedCollection =
    //   this.festivalBonusDetailsService.addFestivalBonusDetailsToCollectionIfMissing<IFestivalBonusDetails>(
    //     this.festivalBonusDetailsSharedCollection,
    //     holdFbDisbursement.festivalBonusDetail
    //   );
  }

  protected loadRelationshipsOptions(): void {
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.holdFbDisbursement?.disbursedBy)))
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
    //
    // this.festivalBonusDetailsService
    //   .query()
    //   .pipe(map((res: HttpResponse<IFestivalBonusDetails[]>) => res.body ?? []))
    //   .pipe(
    //     map((festivalBonusDetails: IFestivalBonusDetails[]) =>
    //       this.festivalBonusDetailsService.addFestivalBonusDetailsToCollectionIfMissing<IFestivalBonusDetails>(
    //         festivalBonusDetails,
    //         this.holdFbDisbursement?.festivalBonusDetail
    //       )
    //     )
    //   )
    //   .subscribe((festivalBonusDetails: IFestivalBonusDetails[]) => (this.festivalBonusDetailsSharedCollection = festivalBonusDetails));
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
