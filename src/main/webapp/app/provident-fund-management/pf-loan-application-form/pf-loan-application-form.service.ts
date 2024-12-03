import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import dayjs from 'dayjs/esm';
import { IPfLoanApplication } from '../../shared/legacy/legacy-model/pf-loan-application.model';

type EntityResponseType = HttpResponse<IPfLoanApplication>;
type EntityArrayResponseType = HttpResponse<IPfLoanApplication[]>;

@Injectable({ providedIn: 'root' })
export class PfLoanApplicationFormService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api/common/pf/pf-loan-application-form');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfLoanApplication: IPfLoanApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanApplication);
    return this.http
      .post<IPfLoanApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pfLoanApplication: IPfLoanApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanApplication);
    return this.http
      .put<IPfLoanApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getEmployeePfLoanEligibleAmount(): Observable<number> {
    return this.http.get<number>(this.resourceUrl + '/pf-loan-eligible-amount');
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPfLoanApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  hasAnyPfAccountForThisUser(): Observable<boolean> {
    return this.http.get<boolean>(this.resourceUrl + '/check-any-pf-account');
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPfLoanApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(pfLoanApplication: IPfLoanApplication): IPfLoanApplication {
    const copy: IPfLoanApplication = Object.assign({}, pfLoanApplication, {
      recommendationDate:
        pfLoanApplication.recommendationDate && pfLoanApplication.recommendationDate.isValid()
          ? pfLoanApplication.recommendationDate.format(DATE_FORMAT)
          : undefined,
      approvalDate:
        pfLoanApplication.approvalDate && pfLoanApplication.approvalDate.isValid()
          ? pfLoanApplication.approvalDate.format(DATE_FORMAT)
          : undefined,
      rejectionDate:
        pfLoanApplication.rejectionDate && pfLoanApplication.rejectionDate.isValid()
          ? pfLoanApplication.rejectionDate.format(DATE_FORMAT)
          : undefined,
      disbursementDate:
        pfLoanApplication.disbursementDate && pfLoanApplication.disbursementDate.isValid()
          ? pfLoanApplication.disbursementDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.recommendationDate = res.body.recommendationDate ? dayjs(res.body.recommendationDate) : undefined;
      res.body.approvalDate = res.body.approvalDate ? dayjs(res.body.approvalDate) : undefined;
      res.body.rejectionDate = res.body.rejectionDate ? dayjs(res.body.rejectionDate) : undefined;
      res.body.disbursementDate = res.body.disbursementDate ? dayjs(res.body.disbursementDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pfLoanApplication: IPfLoanApplication) => {
        pfLoanApplication.recommendationDate = pfLoanApplication.recommendationDate
          ? dayjs(pfLoanApplication.recommendationDate)
          : undefined;
        pfLoanApplication.approvalDate = pfLoanApplication.approvalDate ? dayjs(pfLoanApplication.approvalDate) : undefined;
        pfLoanApplication.rejectionDate = pfLoanApplication.rejectionDate ? dayjs(pfLoanApplication.rejectionDate) : undefined;
        pfLoanApplication.disbursementDate = pfLoanApplication.disbursementDate ? dayjs(pfLoanApplication.disbursementDate) : undefined;
      });
    }
    return res;
  }
}
