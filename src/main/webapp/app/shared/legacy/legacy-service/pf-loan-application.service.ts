import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IEmployeeBankDetails } from 'app/shared/model/employee-bank-details.model';
import { IPfLoanApplicationEligible, PfLoanApplicationEligible } from 'app/shared/model/pf-loan-application-eligible.model';
import { IPfLoanApplication } from '../legacy-model/pf-loan-application.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IPfLoanApplication>;
type EmployeeBankDetailsResponseType = HttpResponse<IEmployeeBankDetails>;
type EntityArrayResponseType = HttpResponse<IPfLoanApplication[]>;

@Injectable({ providedIn: 'root' })
export class PfLoanApplicationService {
  public resourceUrl = SERVER_API_URL + 'api/pf-mgt/pf-loan-applications';

  constructor(protected http: HttpClient) {}

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

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPfLoanApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPfLoanApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getPfLoanApplicationEligibility(pfAccountId: number): Observable<HttpResponse<PfLoanApplicationEligible>> {
    return this.http.get<IPfLoanApplicationEligible>(this.resourceUrl + '/check-eligibility/' + pfAccountId, { observe: 'response' });
  }

  getEmployeeBankDetailsByPin(pin: string): Observable<EmployeeBankDetailsResponseType> {
    return this.http.get<IEmployeeBankDetails>(this.resourceUrl + '/bank-details/employee/' + pin, { observe: 'response' });
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
