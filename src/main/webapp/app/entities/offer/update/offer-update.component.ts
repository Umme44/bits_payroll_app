import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { OfferFormService, OfferFormGroup } from './offer-form.service';
import { IOffer } from '../offer.model';
import { OfferService } from '../service/offer.service';

@Component({
  selector: 'jhi-offer-update',
  templateUrl: './offer-update.component.html',
})
export class OfferUpdateComponent implements OnInit {
  isSaving = false;
  offer: IOffer | null = null;

  offerImage!: File;

  isValidFileType!: boolean;
  isValidFileSize!: boolean;
  anyFileSelected!: boolean;

  editForm: OfferFormGroup = this.offerFormService.createOfferFormGroup();

  constructor(
    protected offerService: OfferService,
    protected offerFormService: OfferFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offer }) => {
      this.offer = offer;
      if (offer) {
        this.updateForm(offer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const offer = this.offerFormService.getOffer(this.editForm);
    if (offer.id !== null) {
      if (this.anyFileSelected) {
        this.subscribeToSaveResponse(this.offerService.updateWithMultipartData(this.offerImage, offer));
      } else {
        this.subscribeToSaveResponse(this.offerService.update(offer));
      }
    } else {
      this.subscribeToSaveResponse(this.offerService.create(this.offerImage, offer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOffer>>): void {
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

  protected updateForm(offer: IOffer): void {
    this.offer = offer;
    this.offerFormService.resetForm(this.editForm, offer);
  }

  onChangeOfferImagePath(event: any): void {
    this.isValidFileSize = true;
    this.isValidFileType = true;
    if (event.target.files != null && event.target.files.length > 0) {
      const file = event.target.files[0];

      file && (file.type === 'image/jpeg' || file.type === 'image/png') ? (this.isValidFileType = true) : (this.isValidFileType = false);

      if (this.isValidFileType) {
        const sizeInKB = Number(file.size / 1024);
        sizeInKB <= 2048 ? (this.isValidFileSize = true) : (this.isValidFileSize = false);

        if (this.isValidFileSize) {
          this.offerImage = file;
          this.anyFileSelected = true;
        }
      }
    } else {
      this.anyFileSelected = false;
    }
  }

}
