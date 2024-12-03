import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { FestivalFormGroup, FestivalFormService } from './festival-form.service';
import { IFestival } from '../festival.model';
import { FestivalService } from '../service/festival.service';
import { Religion } from 'app/entities/enumerations/religion.model';

import { HttpHeaders } from '@angular/common/http';
import {Filter} from "../../../common/employee-address-book/filter.model";
import { Data, ParamMap, Router } from '@angular/router';
import {ITEMS_PER_PAGE} from "../../../config/pagination.constants";
import {
  swalClose,
  swalOnGenerateSuccess,
  swalOnLoading, swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSelection
} from "../../../shared/swal-common/swal-common";

@Component({
  selector: 'jhi-festival-update',
  templateUrl: './festival-update.component.html',
})
export class FestivalUpdateComponent implements OnInit {
  isSaving = false;
  festival: IFestival | null = null;
  religionValues = Object.keys(Religion);

  editForm: FestivalFormGroup = this.festivalFormService.createFestivalFormGroup();
  festivals?: IFestival[];
  totalItems = 0;

  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  itemsPerPage = ITEMS_PER_PAGE;

  filterDTo: Filter;
  pageType!: string;

  constructor(
    protected festivalService: FestivalService,
    protected festivalFormService: FestivalFormService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
  ) {}


  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ festival }) => {
      this.updateForm(festival);
      if (!festival) {
        this.swalForSelection();
      } else {
        if (festival.isProRata) {
          this.pageType = 'pro-rata';
        } else {
          this.pageType = 'general';
        }
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    swalOnLoading('Festival Bonus generation has started. Please wait');
    const festival = this.festivalFormService.getFestival(this.editForm);
    if (festival.id !== null) {
      this.subscribeToSaveResponse(this.festivalService.update(festival));
    } else {
      this.subscribeToSaveResponse(this.festivalService.create(festival as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFestival>>): void {
    result.subscribe(
      res => this.onSaveSuccess(res.body!),
      errorObj => this.onSaveError(errorObj)
    );
  }

  protected onSaveSuccess(festival: IFestival): void {
    swalClose();
    if (!this.editForm.get('id')!.value) {
      this.router.navigate(['/festival/' + festival.id + '/view']);
      swalOnGenerateSuccess('Generated');
    } else {
      this.router.navigate(['/festival/' + festival.id + '/view']);
      swalOnGenerateSuccess('Re-Generated');
    }
    this.isSaving = false;
  }

  protected onSaveError(errObj: any): void {
    swalOnRequestErrorWithBackEndErrorTitle(errObj.error.title, 5000);
    this.isSaving = false;
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(festival: IFestival): void {
    this.festival = festival;
    this.festivalFormService.resetForm(this.editForm, festival);
  }

  protected onSuccess(data: IFestival[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/festival'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.festivals = data || [];
    this.ngbPaginationPage = this.page;
  }

  swalForSelection(): void {
    swalOnSelection('General', 'Pro Rata', false).then(result => {
      if (result.isConfirmed) {
        this.pageType = 'general';
      } else {
        this.pageType = 'pro-rata';
        this.editForm.get('isProRata')!.setValue(true);
        this.editForm.get('religion')!.setValue(Religion.ALL);
      }
    });
  }
}
