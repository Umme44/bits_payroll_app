import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IncomeTaxChallanFormGroup, IncomeTaxChallanFormService } from './income-tax-challan-form.service';
import { IIncomeTaxChallan } from '../income-tax-challan.model';
import { IncomeTaxChallanService } from '../service/income-tax-challan.service';
import { IAitConfig } from 'app/entities/ait-config/ait-config.model';
import { AitConfigService } from 'app/entities/ait-config/service/ait-config.service';
import { Month } from 'app/entities/enumerations/month.model';

@Component({
  selector: 'jhi-income-tax-challan-update',
  templateUrl: './income-tax-challan-update.component.html',
})
export class IncomeTaxChallanUpdateComponent implements OnInit {
  isSaving = false;
  incomeTaxChallan: IIncomeTaxChallan | null = null;
  monthValues = Object.keys(Month);
  aitConfigsSharedCollection: IAitConfig[] = [];
  challanDateDp: any;

  isDuplicateTitle!: Boolean;
  saveExistsTitle?: string;

  currentYear: number = new Date().getFullYear();
  years: number[];

  editForm: IncomeTaxChallanFormGroup = this.incomeTaxChallanFormService.createIncomeTaxChallanFormGroup();

  constructor(
    protected incomeTaxChallanService: IncomeTaxChallanService,
    protected incomeTaxChallanFormService: IncomeTaxChallanFormService,
    protected aitConfigService: AitConfigService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];
  }

  compareAitConfig = (o1: IAitConfig | null, o2: IAitConfig | null): boolean => this.aitConfigService.compareAitConfig(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeTaxChallan }) => {
      this.incomeTaxChallan = incomeTaxChallan;
      if (incomeTaxChallan) {
        this.updateForm(incomeTaxChallan);
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let incomeTaxChallan = this.incomeTaxChallanFormService.getIncomeTaxChallan(this.editForm);
    const selectedAitConfigId = incomeTaxChallan.aitConfig?.id;
    this.incomeTaxChallan.aitConfigId = selectedAitConfigId;
    delete incomeTaxChallan.aitConfig;
    if (incomeTaxChallan.id !== null) {
      this.subscribeToSaveResponse(this.incomeTaxChallanService.update(incomeTaxChallan));
    } else {
      this.subscribeToSaveResponse(this.incomeTaxChallanService.create(incomeTaxChallan as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncomeTaxChallan>>): void {
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

  protected updateForm(incomeTaxChallan: IIncomeTaxChallan): void {
    this.incomeTaxChallan = incomeTaxChallan;
    this.incomeTaxChallanFormService.resetForm(this.editForm, incomeTaxChallan);

    this.aitConfigsSharedCollection = this.aitConfigService.addAitConfigToCollectionIfMissing<IAitConfig>(
      this.aitConfigsSharedCollection,
      incomeTaxChallan.aitConfig
    );
  }

  protected loadRelationshipsOptions(): void {
    this.aitConfigService
      .query()
      .pipe(map((res: HttpResponse<IAitConfig[]>) => res.body ?? []))
      .pipe(
        map((aitConfigs: IAitConfig[]) =>
          this.aitConfigService.addAitConfigToCollectionIfMissing<IAitConfig>(aitConfigs, this.incomeTaxChallan?.aitConfig)
        )
      )
      .subscribe((aitConfigs: IAitConfig[]) => (this.aitConfigsSharedCollection = aitConfigs));
  }

  checkDuplicateChallanNo(): void {
    const challanNo = this.editForm.get(['challanNo'])!.value;
    if (this.saveExistsTitle === challanNo || challanNo.length === 0) {
      this.isDuplicateTitle = false;
    } else {
      this.incomeTaxChallanService.checkChallanNo(challanNo).subscribe(res => {
        this.isDuplicateTitle = res.body!;
      });
    }
  }
}
