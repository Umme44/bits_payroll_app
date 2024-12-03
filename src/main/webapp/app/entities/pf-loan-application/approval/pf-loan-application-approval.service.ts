import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IPfLoanApplicationForm } from './pf-loan-application-form.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm'
import {
  IPfLoanApplicationEligible,
  PfLoanApplicationEligible
} from '../../../shared/model/pf-loan-application-eligible.model';
import { IEmployeeBankDetails } from '../../../shared/model/employee-bank-details.model';

type EntityResponseType = HttpResponse<IPfLoanApplicationForm>;
type EntityArrayResponseType = HttpResponse<IPfLoanApplicationForm[]>;
type EmployeeBankDetailsResponseType = HttpResponse<IEmployeeBankDetails>;

@Injectable({ providedIn: 'root' })
export class PfLoanApplicationFormApprovalService {
  public resourceUrl = SERVER_API_URL + 'api/pf-mgt/pf-loan-applications';

  constructor(protected http: HttpClient) {}

  approvePfLoanApplication(pfLoanApplicationForm: IPfLoanApplicationForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanApplicationForm);
    return this.http
      .put<IPfLoanApplicationForm>(this.resourceUrl + '/hr/approval', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  rejectPfLoanApplication(pfLoanApplicationForm: IPfLoanApplicationForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoanApplicationForm);
    return this.http
      .put<IPfLoanApplicationForm>(this.resourceUrl + '/hr/reject', copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromClient(pfLoanApplicationForm: IPfLoanApplicationForm): IPfLoanApplicationForm {
    const copy: IPfLoanApplicationForm = Object.assign({}, pfLoanApplicationForm, {
      disbursementDate:
        pfLoanApplicationForm.disbursementDate && pfLoanApplicationForm.disbursementDate.isValid()
          ? pfLoanApplicationForm.disbursementDate.format(DATE_FORMAT)
          : undefined,
      instalmentStartFrom:
        pfLoanApplicationForm.instalmentStartFrom && pfLoanApplicationForm.instalmentStartFrom.isValid()
          ? pfLoanApplicationForm.instalmentStartFrom.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.disbursementDate = res.body.disbursementDate ? dayjs(res.body.disbursementDate) : undefined;
      res.body.instalmentStartFrom = res.body.instalmentStartFrom ? dayjs(res.body.instalmentStartFrom) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pfLoanApplicationForm: IPfLoanApplicationForm) => {
        pfLoanApplicationForm.disbursementDate = pfLoanApplicationForm.disbursementDate
          ? dayjs(pfLoanApplicationForm.disbursementDate)
          : undefined;
        pfLoanApplicationForm.instalmentStartFrom = pfLoanApplicationForm.instalmentStartFrom
          ? dayjs(pfLoanApplicationForm.instalmentStartFrom)
          : undefined;
      });
    }
    return res;
  }

  getPfLoanApplicationEligibility(pfAccountId: number): Observable<HttpResponse<PfLoanApplicationEligible>> {
    return this.http.get<IPfLoanApplicationEligible>(this.resourceUrl + '/check-eligibility/' + pfAccountId, { observe: 'response' });
  }

  getEmployeeBankDetailsByPin(pin: string): Observable<EmployeeBankDetailsResponseType> {
    return this.http.get<IEmployeeBankDetails>(this.resourceUrl + '/bank-details/employee/' + pin, { observe: 'response' });
  }
}
