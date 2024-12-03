import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserTaxAcknowledgementValidation } from 'app/shared/model/user-tax-acknowledgement-validation.model';
import { ITaxAcknowledgementReceipt } from '../legacy-model/tax-acknowledgement-receipt.model';
import { INominee } from '../legacy-model/nominee.model';
import { IEmployeeMinimal } from '../../model/employee-minimal.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm';

type EntityResponseType = HttpResponse<ITaxAcknowledgementReceipt>;
type EntityArrayResponseType = HttpResponse<ITaxAcknowledgementReceipt[]>;

@Injectable({ providedIn: 'root' })
export class UserTaxAcknowledgementReceiptService {
  public resourceUrl = SERVER_API_URL + 'api/common/tax-acknowledgement-receipts';

  constructor(protected http: HttpClient) {}

  createCommon(file: File, taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    formData.append('tax-acknowledgement', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .post<INominee>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    return this.http
      .put<ITaxAcknowledgementReceipt>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  updateWithFile(file: File, taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const copy = this.convertDateFromClient(taxAcknowledgementReceipt);
    formData.append('tax-acknowledgement', new Blob([JSON.stringify(copy)], { type: 'application/json' }));
    return this.http
      .put<INominee>(`${this.resourceUrl}/multipart`, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getCurrentEmployeeInfo(): Observable<HttpResponse<IEmployeeMinimal>> {
    return this.http
      .get<IEmployeeMinimal>(`api/common/tax-acknowledgement-receipts/current-employee-info`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITaxAcknowledgementReceipt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaxAcknowledgementReceipt[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): ITaxAcknowledgementReceipt {
    const copy: ITaxAcknowledgementReceipt = Object.assign({}, taxAcknowledgementReceipt, {
      dateOfSubmission:
        taxAcknowledgementReceipt.dateOfSubmission && taxAcknowledgementReceipt.dateOfSubmission.isValid()
          ? taxAcknowledgementReceipt.dateOfSubmission.format(DATE_FORMAT)
          : undefined,
      receivedAt:
        taxAcknowledgementReceipt.receivedAt && taxAcknowledgementReceipt.receivedAt.isValid()
          ? taxAcknowledgementReceipt.receivedAt.toJSON()
          : undefined,
      createdAt:
        taxAcknowledgementReceipt.createdAt && taxAcknowledgementReceipt.createdAt.isValid()
          ? taxAcknowledgementReceipt.createdAt.toJSON()
          : undefined,
      updatedAt:
        taxAcknowledgementReceipt.updatedAt && taxAcknowledgementReceipt.updatedAt.isValid()
          ? taxAcknowledgementReceipt.updatedAt.toJSON()
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfSubmission = res.body.dateOfSubmission ? dayjs(res.body.dateOfSubmission) : undefined;
      res.body.receivedAt = res.body.receivedAt ? dayjs(res.body.receivedAt) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((taxAcknowledgementReceipt: ITaxAcknowledgementReceipt) => {
        taxAcknowledgementReceipt.dateOfSubmission = taxAcknowledgementReceipt.dateOfSubmission
          ? dayjs(taxAcknowledgementReceipt.dateOfSubmission)
          : undefined;
        taxAcknowledgementReceipt.receivedAt = taxAcknowledgementReceipt.receivedAt
          ? dayjs(taxAcknowledgementReceipt.receivedAt)
          : undefined;
        taxAcknowledgementReceipt.createdAt = taxAcknowledgementReceipt.createdAt ? dayjs(taxAcknowledgementReceipt.createdAt) : undefined;
        taxAcknowledgementReceipt.updatedAt = taxAcknowledgementReceipt.updatedAt ? dayjs(taxAcknowledgementReceipt.updatedAt) : undefined;
      });
    }
    return res;
  }

  downloadTaxAcknowledgementReport(id: number): Observable<HttpResponse<Blob>> {
    const uri = this.resourceUrl + '/download/' + id;
    return this.http.get<Blob>(uri, { responseType: 'blob' as 'json', observe: 'response' });
  }

  checkUserTaxAcknowledgementReceiptValidation(): Observable<HttpResponse<UserTaxAcknowledgementValidation>> {
    return this.http.get<UserTaxAcknowledgementValidation>(`${this.resourceUrl}/check-user-tax-info-validation`, { observe: 'response' });
  }
}
