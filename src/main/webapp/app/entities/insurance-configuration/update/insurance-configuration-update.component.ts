import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InsuranceConfigurationFormGroup, InsuranceConfigurationFormService } from './insurance-configuration-form.service';
import { IInsuranceConfiguration } from '../insurance-configuration.model';
import { InsuranceConfigurationService } from '../service/insurance-configuration.service';

@Component({
  selector: 'jhi-insurance-configuration-update',
  templateUrl: './insurance-configuration-update.component.html',
})
export class InsuranceConfigurationUpdateComponent implements OnInit {
  isSaving = false;
  insuranceConfiguration: IInsuranceConfiguration | null = null;

  editForm: InsuranceConfigurationFormGroup = this.insuranceConfigurationFormService.createInsuranceConfigurationFormGroup();

  constructor(
    protected insuranceConfigurationService: InsuranceConfigurationService,
    protected insuranceConfigurationFormService: InsuranceConfigurationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ insuranceConfiguration }) => {
      this.insuranceConfiguration = insuranceConfiguration;
      if (insuranceConfiguration) {
        this.updateForm(insuranceConfiguration);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insuranceConfiguration = this.insuranceConfigurationFormService.getInsuranceConfiguration(this.editForm);
    if (insuranceConfiguration.id !== null) {
      this.subscribeToSaveResponse(this.insuranceConfigurationService.update(insuranceConfiguration));
    } else {
      this.subscribeToSaveResponse(this.insuranceConfigurationService.create(insuranceConfiguration as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsuranceConfiguration>>): void {
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

  protected updateForm(insuranceConfiguration: IInsuranceConfiguration): void {
    this.insuranceConfiguration = insuranceConfiguration;
    this.insuranceConfigurationFormService.resetForm(this.editForm, insuranceConfiguration);
  }
}
