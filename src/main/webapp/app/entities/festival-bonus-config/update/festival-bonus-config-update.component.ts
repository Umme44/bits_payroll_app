import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FestivalBonusConfigFormGroup, FestivalBonusConfigFormService } from './festival-bonus-config-form.service';
import { IFestivalBonusConfig } from '../festival-bonus-config.model';
import { FestivalBonusConfigService } from '../service/festival-bonus-config.service';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';

@Component({
  selector: 'jhi-festival-bonus-config-update',
  templateUrl: './festival-bonus-config-update.component.html',
})
export class FestivalBonusConfigUpdateComponent implements OnInit {
  isSaving = false;
  festivalBonusConfig: IFestivalBonusConfig | null = null;
  employeeCategoryValues = Object.keys(EmployeeCategory);

  editForm: FestivalBonusConfigFormGroup = this.festivalBonusConfigFormService.createFestivalBonusConfigFormGroup();

  constructor(
    protected festivalBonusConfigService: FestivalBonusConfigService,
    protected festivalBonusConfigFormService: FestivalBonusConfigFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ festivalBonusConfig }) => {
      this.festivalBonusConfig = festivalBonusConfig;
      if (festivalBonusConfig) {
        this.updateForm(festivalBonusConfig);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const festivalBonusConfig = this.festivalBonusConfigFormService.getFestivalBonusConfig(this.editForm);
    if (festivalBonusConfig.id !== null) {
      this.subscribeToSaveResponse(this.festivalBonusConfigService.update(festivalBonusConfig));
    } else {
      this.subscribeToSaveResponse(this.festivalBonusConfigService.create(festivalBonusConfig as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFestivalBonusConfig>>): void {
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

  protected updateForm(festivalBonusConfig: IFestivalBonusConfig): void {
    this.festivalBonusConfig = festivalBonusConfig;
    this.festivalBonusConfigFormService.resetForm(this.editForm, festivalBonusConfig);
  }
}
