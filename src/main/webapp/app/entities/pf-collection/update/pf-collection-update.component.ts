import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PfCollectionFormService, PfCollectionFormGroup } from './pf-collection-form.service';
import { IPfCollection } from '../pf-collection.model';
import { PfCollectionService } from '../service/pf-collection.service';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';
import { PfCollectionType } from 'app/entities/enumerations/pf-collection-type.model';

import { FormBuilder, Validators, FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-pf-collection-update',
  templateUrl: './pf-collection-update.component.html',
})
export class PfCollectionUpdateComponent implements OnInit {
  isSaving = false;
  pfCollection: IPfCollection | null = null;
  pfCollectionTypeValues = Object.keys(PfCollectionType);

  pfAccountsSharedCollection: IPfAccount[] = [];

  editForm: PfCollectionFormGroup = this.pfCollectionFormService.createPfCollectionFormGroup();

  constructor(
    protected pfCollectionService: PfCollectionService,
    protected pfCollectionFormService: PfCollectionFormService,
    protected pfAccountService: PfAccountService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePfAccount = (o1: IPfAccount | null, o2: IPfAccount | null): boolean => this.pfAccountService.comparePfAccount(o1, o2);
  months = [
    { Value: 1, Text: 'January' },
    { Value: 2, Text: 'February' },
    { Value: 3, Text: 'March' },
    { Value: 4, Text: 'April' },
    { Value: 5, Text: 'May' },
    { Value: 6, Text: 'June' },
    { Value: 7, Text: 'July' },
    { Value: 8, Text: 'August' },
    { Value: 9, Text: 'September' },
    { Value: 10, Text: 'October' },
    { Value: 11, Text: 'November' },
    { Value: 12, Text: 'December' },
  ];
  selectedPfAccountId!: number;
  /* get pfAccountIdForm(): FormGroup {
    return this.editForm.get('pfAccountId') as FormGroup;
  } */

  get pfAccountIdForm(): FormGroup {
    const newFormGroup = new FormGroup({
      pfAccountId: this.editForm.get('pfAccountId'),
    });
    return newFormGroup;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfCollection }) => {
      this.pfCollection = pfCollection;
      if (pfCollection) {
        this.updateForm(pfCollection);
        this.selectedPfAccountId = pfCollection.pfAccountId;
        this.editForm.get('collectionType').disable();
        this.editForm.get('year').disable();
        this.editForm.get('month').disable();
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pfCollection = this.pfCollectionFormService.getPfCollection(this.editForm);
    if (pfCollection.id !== null) {
      this.subscribeToSaveResponse(this.pfCollectionService.update(pfCollection));
    } else {
      this.subscribeToSaveResponse(this.pfCollectionService.create(pfCollection as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfCollection>>): void {
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

  protected updateForm(pfCollection: IPfCollection): void {
    this.pfCollection = pfCollection;
    this.pfCollectionFormService.resetForm(this.editForm, pfCollection);

    // this.pfAccountsSharedCollection = this.pfAccountService.addPfAccountToCollectionIfMissing<IPfAccount>(
    //   this.pfAccountsSharedCollection,
    //   pfCollection.pfAccount
    // );
  }

  protected loadRelationshipsOptions(): void {
    //this.pfCollectionService.
    // this.pfAccountService
    //   .query()
    //   .pipe(map((res: HttpResponse<IPfAccount[]>) => res.body ?? []))
    //   .pipe(
    //     map((pfAccounts: IPfAccount[]) =>
    //       this.pfAccountService.addPfAccountToCollectionIfMissing<IPfAccount>(pfAccounts, this.pfCollection?.pfAccount)
    //     )
    //   )
    //   .subscribe((pfAccounts: IPfAccount[]) => (this.pfAccountsSharedCollection = pfAccounts));
  }

  patchSelectedEmployee($event: any) {
    this.editForm.patchValue({
      pfAccountId: $event.id,
    });
  }
}
