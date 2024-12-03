import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPfLoanRepayment } from '../legacy-model/pf-loan-repayment.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from '../../../config/input.constants';
import { createRequestOption } from '../../../core/request/request-util';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IPfLoanRepayment>;
type EntityArrayResponseType = HttpResponse<IPfLoanRepayment[]>;

@Injectable({ providedIn: 'root' })
export class PfLoanRepaymentService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/pf-loan-repayments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfLoanRepayment: IPfLoanRepayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanRepayment);
    return this.http
      .post<IPfLoanRepayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pfLoanRepayment: IPfLoanRepayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanRepayment);
    return this.http
      .put<IPfLoanRepayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPfLoanRepayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPfLoanRepayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  uploadXlsxFile(file: File, year: number, month: number): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/xlsx-upload/${year}/${month}`, formData, { observe: 'response' });
  }

  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPfLoanRepayment[]>(`${this.resourceUrl}/${year}/${month}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(pfLoanRepayment: IPfLoanRepayment): IPfLoanRepayment {
    const copy: IPfLoanRepayment = Object.assign({}, pfLoanRepayment, {
      deductionDate:
        pfLoanRepayment.deductionDate && pfLoanRepayment.deductionDate.isValid()
          ? pfLoanRepayment.deductionDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.deductionDate = res.body.deductionDate ? dayjs(res.body.deductionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pfLoanRepayment: IPfLoanRepayment) => {
        pfLoanRepayment.deductionDate = pfLoanRepayment.deductionDate ? dayjs(pfLoanRepayment.deductionDate) : undefined;
      });
    }
    return res;
  }
}
