import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IInsuranceConfiguration } from '../insurance-configuration.model';
import { InsuranceConfigurationService } from '../service/insurance-configuration.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { InsuranceConfigurationDeleteDialogComponent } from '../delete/insurance-configuration-delete-dialog.component';
@Component({
  selector: 'jhi-insurance-configuration',
  templateUrl: './insurance-configuration.component.html',
})
export class InsuranceConfigurationComponent implements OnInit, OnDestroy {
  insuranceConfigurations: IInsuranceConfiguration[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected insuranceConfigurationService: InsuranceConfigurationService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.insuranceConfigurations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.insuranceConfigurationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IInsuranceConfiguration[]>) => this.paginateInsuranceConfigurations(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.insuranceConfigurations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInsuranceConfigurations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInsuranceConfiguration): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInsuranceConfigurations(): void {
    this.eventSubscriber = this.eventManager.subscribe('insuranceConfigurationListModification', () => this.reset());
  }

  delete(insuranceConfiguration: IInsuranceConfiguration): void {
    const modalRef = this.modalService.open(InsuranceConfigurationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.insuranceConfiguration = insuranceConfiguration;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateInsuranceConfigurations(data: IInsuranceConfiguration[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.insuranceConfigurations.push(data[i]);
      }
    }
  }
}
