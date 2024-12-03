import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

import { SWAL_CONFIRM_BTN_TEXT, SWAL_DELETE_CONFIRMATION, SWAL_DENY_BTN_TEXT } from 'app/shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from 'app/shared/constants/color.code.constant';
import { swalOnDeleteSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import Swal from 'sweetalert2';
import { ITrainingHistory } from '../../../training-history/training-history.model';
import { IEmployee } from '../../employee-custom.model';
import { TrainingHistoryService } from '../../../training-history/service/training-history.service';

@Component({
  selector: 'jhi-employee-training-history',
  templateUrl: './employee-training-history.modal.component.html',
})
export class EmployeeTrainingHistoryModalComponent implements OnInit {
  trainingHistories?: ITrainingHistory[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  employeeId!: number;
  employee!: IEmployee;
  idForUpdate = -1;

  constructor(
    protected trainingHistoryService: TrainingHistoryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected activeModal: NgbActiveModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.trainingHistoryService.queryByEmployeeId(this.employeeId, {}).subscribe(
      (res: HttpResponse<ITrainingHistory[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
      () => this.onError()
    );
  }

  ngOnInit(): void {
    this.loadPage();
  }

  trackId(index: number, item: ITrainingHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(trainingHistory: ITrainingHistory): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.trainingHistoryService.delete(trainingHistory.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadPage();
          },
          () => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  protected onSuccess(data: ITrainingHistory[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.trainingHistories = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    swalOnRequestError();
  }

  populateEditForm(id: number): void {
    this.idForUpdate = id;
  }

  dismiss(): void {
    this.activeModal.dismiss('Cross click');
  }
}
