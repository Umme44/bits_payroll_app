import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';

import { ILocation, Location } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { DATE_TIME_FORMAT } from '../../config/input.constants';
import { MobileBillFormGroup, MobileBillFormService } from '../mobile-bill/update/mobile-bill-form.service';
import { LocationFormGroup, LocationFormService } from './location-form.service';

@Component({
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html',
})
export class LocationUpdateComponent implements OnInit {
  isSaving = false;
  location: ILocation | null = null;

  locations: ILocation[] = [];

  editForm: LocationFormGroup = this.locationFormService.createLocationFormGroup();

  constructor(
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected locationFormService: LocationFormService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      if (!location.id) {
        const today = dayjs().startOf('day');
        location.createdAt = today;
        location.updateAt = today;
      }

      this.updateForm(location);

      if (!location.id) {
        this.locationService
          .query({
            unpaged: true,
          })
          .subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body || []));
      } else {
        this.locationService
          .getAllNonSuccessorLocations(location.id)
          .subscribe((res: HttpResponse<ILocation[]>) => (this.locations = res.body || []));
      }
    });
  }

  updateForm(location: ILocation): void {
    this.location = location;
    this.locationFormService.resetForm(this.editForm, location);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const location = this.locationFormService.getLocation(this.editForm);

    if (location.id === null) {
      delete location.id;
    }

    if (location.id !== undefined) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ILocation): any {
    return item.id;
  }
}
