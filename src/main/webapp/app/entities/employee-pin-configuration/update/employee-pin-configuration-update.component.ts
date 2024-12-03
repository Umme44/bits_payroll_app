import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeePinConfigurationFormGroup, EmployeePinConfigurationFormService } from './employee-pin-configuration-form.service';
import { IEmployeePinConfiguration } from '../employee-pin-configuration.model';
import { EmployeePinConfigurationService } from '../service/employee-pin-configuration.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';

@Component({
  selector: 'jhi-employee-pin-configuration-update',
  templateUrl: './employee-pin-configuration-update.component.html',
})
export class EmployeePinConfigurationUpdateComponent implements OnInit {
  isSaving = false;
  employeePinConfiguration: IEmployeePinConfiguration | null = null;
  employeeCategoryValues = Object.keys(EmployeeCategory);

  pinSequenceIsUnique = true;

  invalidPinSequence = false;

  usersSharedCollection: IUser[] = [];

  editForm: EmployeePinConfigurationFormGroup = this.employeePinConfigurationFormService.createEmployeePinConfigurationFormGroup();

  constructor(
    protected employeePinConfigurationService: EmployeePinConfigurationService,
    protected employeePinConfigurationFormService: EmployeePinConfigurationFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeePinConfiguration }) => {
      this.employeePinConfiguration = employeePinConfiguration;
      if (employeePinConfiguration) {
        this.updateForm(employeePinConfiguration);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeePinConfiguration = this.employeePinConfigurationFormService.getEmployeePinConfiguration(this.editForm);
    if (employeePinConfiguration.id !== null) {
      this.subscribeToSaveResponse(this.employeePinConfigurationService.update(employeePinConfiguration));
    } else {
      this.subscribeToSaveResponse(this.employeePinConfigurationService.create(employeePinConfiguration as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeePinConfiguration>>): void {
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

  protected updateForm(employeePinConfiguration: IEmployeePinConfiguration): void {
    this.employeePinConfiguration = employeePinConfiguration;
    this.employeePinConfigurationFormService.resetForm(this.editForm, employeePinConfiguration);

    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   employeePinConfiguration.createdBy,
    //   employeePinConfiguration.updatedBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(
    //         users,
    //         this.employeePinConfiguration?.createdBy,
    //         this.employeePinConfiguration?.updatedBy
    //       )
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  onChangePinSequence(): void {
    const startingPin = this.editForm.get(['sequenceStart'])!.value;
    const endingPin = this.editForm.get(['sequenceEnd'])!.value;
    this.pinSequenceIsUnique = true;
    this.invalidPinSequence = false;

    if (startingPin !== null && startingPin !== undefined && endingPin !== null && endingPin !== undefined) {
      if (this.isPinSequenceInValid(startingPin, endingPin) === true) {
        this.invalidPinSequence = true;
        return;
      }
    }

    if (startingPin !== null && startingPin !== undefined && endingPin !== null && endingPin !== undefined) {
      this.employeePinConfigurationService.isPinSequenceUnique(this.getReqObject()).subscribe(res => {
        if (res.body) {
          this.pinSequenceIsUnique = true;
        } else {
          this.pinSequenceIsUnique = false;
        }
      });
    }
  }

  getReqObject(): any {
    const id = this.editForm.get(['id'])!.value;
    const startingPin = this.editForm.get(['sequenceStart'])!.value;
    const endingPin = this.editForm.get(['sequenceEnd'])!.value;

    if (id !== null && id !== undefined) {
      return {
        pinConfigurationId: id,
        startingPin,
        endingPin,
      };
    } else {
      return {
        startingPin,
        endingPin,
      };
    }
  }

  isPinSequenceInValid(startPin: string, endPin: string): boolean {
    if (startPin.trim() === '' || endPin.trim() === '') {
      return true;
    }

    const start = Number.parseInt(startPin.trim(), 10);
    const end = Number.parseInt(endPin.trim(), 10);

    if (start < 0 || end < 0) {
      return true;
    } else if (start > end) {
      return true;
    } else {
      return false;
    }
  }

  onChangeEmployeeCategory(): void {
    this.editForm.get(['sequenceStart'])!.reset();
    this.editForm.get(['sequenceEnd'])!.reset();
  }
}
