import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AitConfigFormGroup, AitConfigFormService } from './ait-config-form.service';
import { IAitConfig } from '../ait-config.model';
import { AitConfigService } from '../service/ait-config.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-ait-config-update',
  templateUrl: './ait-config-update.component.html',
})
export class AitConfigUpdateComponent implements OnInit {
  isSaving = false;
  aitConfig: IAitConfig | null = null;

  editForm: AitConfigFormGroup = this.aitConfigFormService.createAitConfigFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected aitConfigService: AitConfigService,
    protected aitConfigFormService: AitConfigFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aitConfig }) => {
      this.aitConfig = aitConfig;
      if (aitConfig) {
        this.updateForm(aitConfig);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aitConfig = this.aitConfigFormService.getAitConfig(this.editForm);
    if (aitConfig.id !== null) {
      this.subscribeToSaveResponse(this.aitConfigService.update(aitConfig));
    } else {
      this.subscribeToSaveResponse(this.aitConfigService.create(aitConfig as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(err: any): void {
    this.isSaving = false;

    let message = '';

    for (const error of err.error.errors) {
      message += error + '<br>';
    }

    Swal.fire({
      icon: 'error',
      html: message,
      showCloseButton: true,
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(aitConfig: IAitConfig): void {
    this.aitConfig = aitConfig;
    this.aitConfigFormService.resetForm(this.editForm, aitConfig);
  }
}
