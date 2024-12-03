import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BandFormGroup, BandFormService } from './band-form.service';
import { IBand } from '../band.model';
import { BandService } from '../service/band.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-band-update',
  templateUrl: './band-update.component.html',
})
export class BandUpdateComponent implements OnInit {
  isSaving = false;
  band: IBand | null = null;
  invalidSalary = false;

  usersSharedCollection: IUser[] = [];

  editForm: BandFormGroup = this.bandFormService.createBandFormGroup();

  constructor(
    protected bandService: BandService,
    protected bandFormService: BandFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ band }) => {
      this.band = band;
      if (band) {
        this.updateForm(band);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const band = this.bandFormService.getBand(this.editForm);
    if (band.id !== null) {
      this.subscribeToSaveResponse(this.bandService.update(band));
    } else {
      this.subscribeToSaveResponse(this.bandService.create(band as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBand>>): void {
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
    this.showErrorForInvalidInput()
  }

  showErrorForInvalidInput(){
    Swal.fire({
      icon: 'error',
      text: 'Invalid input',
      timer: 3500,
      showConfirmButton: false,
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(band: IBand): void {
    this.band = band;
    this.bandFormService.resetForm(this.editForm, band);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      band.createdBy,
      band.updatedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.band?.createdBy, this.band?.updatedBy))
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  checkSalary(): void {
    const minSalary = this.editForm.get(['minSalary'])!.value;
    const maxSalary = this.editForm.get(['maxSalary'])!.value;

    if (minSalary && maxSalary && minSalary > maxSalary) {
      this.invalidSalary = true;
    } else {
      this.invalidSalary = false;
    }
  }
}
