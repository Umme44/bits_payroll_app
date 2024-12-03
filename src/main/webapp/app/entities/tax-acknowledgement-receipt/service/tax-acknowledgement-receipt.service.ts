import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  ITaxAcknowledgementReceipt, NewTaxAcknowledgementReceipt
} from "../../../payroll-management-system/user-tax-acknowledgement-receipt/tax-acknowledgement-receipt.model";
import {IApprovalDTO} from "../../../shared/model/approval-dto.model";
import {IEmployeeMinimal} from "../../../shared/model/employee-minimal.model";
import {IEmployee} from "../../employee-custom/employee-custom.model";

export type PartialUpdateTaxAcknowledgementReceipt = Partial<ITaxAcknowledgementReceipt> & Pick<ITaxAcknowledgementReceipt, 'id'>;

type RestOf<T extends ITaxAcknowledgementReceipt | NewTaxAcknowledgementReceipt> = Omit<
  T,
  'dateOfSubmission' | 'receivedAt' | 'createdAt' | 'updatedAt'
> & {
  dateOfSubmission?: string | null;
  receivedAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestTaxAcknowledgementReceipt = RestOf<ITaxAcknowledgementReceipt>;

export type NewRestTaxAcknowledgementReceipt = RestOf<NewTaxAcknowledgementReceipt>;

export type PartialUpdateRestTaxAcknowledgementReceipt = RestOf<PartialUpdateTaxAcknowledgementReceipt>;

export type EntityResponseType = HttpResponse<ITaxAcknowledgementReceipt>;
export type EntityArrayResponseType = HttpResponse<ITaxAcknowledgementReceipt[]>;
export type BooleanResponseType = HttpResponse<boolean>;

@Injectable({ providedIn: 'root' })
export class TaxAcknowledgementReceiptService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/common/tax-acknowledgement-receipt-finance');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(taxAcknowledgementReceipt: NewTaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    return this.http
      .post<RestTaxAcknowledgementReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  createTaxAcknowledgement(file: File, taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    formData.append('tax-acknowledgement', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<RestTaxAcknowledgementReceipt>(`${this.resourceUrl}/tax-acknowledgement-receipts`, formData, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    return this.http
      .put<RestTaxAcknowledgementReceipt>(
        `${this.resourceUrl}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  updateWithFile(file: File, taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    formData.append('tax-acknowledgement', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<RestTaxAcknowledgementReceipt>(`${this.resourceUrl}/tax-acknowledgement-receipts/multipart`, formData, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getCurrentEmployeeInfo(employeeId: number): Observable<HttpResponse<IEmployee>> {
    return this.http
      .get<IEmployee>(`${this.resourceUrl}/current-employee-info/${employeeId}`, { observe: 'response' })
  }

  partialUpdate(taxAcknowledgementReceipt: PartialUpdateTaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    return this.http
      .patch<RestTaxAcknowledgementReceipt>(
        `${this.resourceUrl}/${this.getTaxAcknowledgementReceiptIdentifier(taxAcknowledgementReceipt)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTaxAcknowledgementReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findV2(id: number, employeeId: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTaxAcknowledgementReceipt>(`${this.resourceUrl}/${id}/${employeeId}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getTotalPendingTaxReceipts(): Observable<number> {
    return this.http.get<number>(`${this.resourceUrl}/getTotalSubmittedReceipts`);
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTaxAcknowledgementReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  enableSelectedTax(approvalDTO: IApprovalDTO): Observable<BooleanResponseType> {
    return this.http.post<boolean>(`${this.resourceUrl}/turn-into-received`, approvalDTO, { observe: 'response' });
  }

  getReceivedTaxAcknowledgementReceipts(req?: any): Observable<EntityArrayResponseType> {
    // null or undefined remove from req or option object
    Object.keys(req).forEach(k => req[k] === null && delete req[k]);
    Object.keys(req).forEach(k => req[k] === undefined && delete req[k]);
    const options = createRequestOption(req);
    return this.http
      .get<RestTaxAcknowledgementReceipt[]>(`${this.resourceUrl}/received-list`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getSubmittedTaxAcknowledgementReceipts(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTaxAcknowledgementReceipt[]>(`${this.resourceUrl}/submitted-list`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  public taxAcknowledgementReceiptExportDownload(aitConfigId: number): Observable<Blob> {
    return this.http.get(`/api/common/export-tax-acknowledgement-year-wise/${aitConfigId}`, { responseType: 'blob' });
  }

  downloadTaxAcknowledgement(id: number): Observable<HttpResponse<Blob>> {
    const uri = this.resourceUrl + '/download/' + id;
    return this.http.get<Blob>(uri, { responseType: 'blob' as 'json', observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTaxAcknowledgementReceiptIdentifier(taxAcknowledgementReceipt: Pick<ITaxAcknowledgementReceipt, 'id'>): number {
    return taxAcknowledgementReceipt.id;
  }

  compareTaxAcknowledgementReceipt(
    o1: Pick<ITaxAcknowledgementReceipt, 'id'> | null,
    o2: Pick<ITaxAcknowledgementReceipt, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getTaxAcknowledgementReceiptIdentifier(o1) === this.getTaxAcknowledgementReceiptIdentifier(o2) : o1 === o2;
  }

  addTaxAcknowledgementReceiptToCollectionIfMissing<Type extends Pick<ITaxAcknowledgementReceipt, 'id'>>(
    taxAcknowledgementReceiptCollection: Type[],
    ...taxAcknowledgementReceiptsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const taxAcknowledgementReceipts: Type[] = taxAcknowledgementReceiptsToCheck.filter(isPresent);
    if (taxAcknowledgementReceipts.length > 0) {
      const taxAcknowledgementReceiptCollectionIdentifiers = taxAcknowledgementReceiptCollection.map(
        taxAcknowledgementReceiptItem => this.getTaxAcknowledgementReceiptIdentifier(taxAcknowledgementReceiptItem)!
      );
      const taxAcknowledgementReceiptsToAdd = taxAcknowledgementReceipts.filter(taxAcknowledgementReceiptItem => {
        const taxAcknowledgementReceiptIdentifier = this.getTaxAcknowledgementReceiptIdentifier(taxAcknowledgementReceiptItem);
        if (taxAcknowledgementReceiptCollectionIdentifiers.includes(taxAcknowledgementReceiptIdentifier)) {
          return false;
        }
        taxAcknowledgementReceiptCollectionIdentifiers.push(taxAcknowledgementReceiptIdentifier);
        return true;
      });
      return [...taxAcknowledgementReceiptsToAdd, ...taxAcknowledgementReceiptCollection];
    }
    return taxAcknowledgementReceiptCollection;
  }

  protected convertDateFromClient<
    T extends ITaxAcknowledgementReceipt | NewTaxAcknowledgementReceipt | PartialUpdateTaxAcknowledgementReceipt
  >(taxAcknowledgementReceipt: T): RestOf<T> {
    return {
      ...taxAcknowledgementReceipt,
      dateOfSubmission: taxAcknowledgementReceipt.dateOfSubmission?.format(DATE_FORMAT) ?? null,
      receivedAt: taxAcknowledgementReceipt.receivedAt?.toJSON() ?? null,
      createdAt: taxAcknowledgementReceipt.createdAt?.toJSON() ?? null,
      updatedAt: taxAcknowledgementReceipt.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTaxAcknowledgementReceipt: RestTaxAcknowledgementReceipt): ITaxAcknowledgementReceipt {
    return {
      ...restTaxAcknowledgementReceipt,
      dateOfSubmission: restTaxAcknowledgementReceipt.dateOfSubmission ? dayjs(restTaxAcknowledgementReceipt.dateOfSubmission) : undefined,
      receivedAt: restTaxAcknowledgementReceipt.receivedAt ? dayjs(restTaxAcknowledgementReceipt.receivedAt) : undefined,
      createdAt: restTaxAcknowledgementReceipt.createdAt ? dayjs(restTaxAcknowledgementReceipt.createdAt) : undefined,
      updatedAt: restTaxAcknowledgementReceipt.updatedAt ? dayjs(restTaxAcknowledgementReceipt.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTaxAcknowledgementReceipt>): HttpResponse<ITaxAcknowledgementReceipt> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTaxAcknowledgementReceipt[]>): HttpResponse<ITaxAcknowledgementReceipt[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
