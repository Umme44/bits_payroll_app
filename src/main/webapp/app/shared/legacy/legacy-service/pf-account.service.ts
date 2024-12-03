import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IUserPfStatement } from 'app/shared/model/user-pf/user-pf-statement.model';
import { IPfAccount } from '../legacy-model/pf-account.model';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IPfAccount>;
type EntityArrayResponseType = HttpResponse<IPfAccount[]>;

@Injectable({ providedIn: 'root' })
export class PfAccountService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt/pf-accounts');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfAccount: IPfAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfAccount);
    return this.http
      .post<IPfAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pfAccount: IPfAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfAccount);
    return this.http
      .put<IPfAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPfAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPfAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getAllPfAccountsList(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPfAccount[]>(this.resourceUrl + '/list', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getCurrentUserPfAccounts(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPfAccount[]>(this.applicationConfigService.getEndpointFor('api/common/pf/pf-loan-application-form/pf-accounts'), {
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  generateUserPfStatement(pfAccountId: number, date: any): Observable<EntityResponseType> {
    const selectedDate = dayjs(date).format(DATE_FORMAT);
    return this.http.get<IUserPfStatement>(
      this.applicationConfigService.getEndpointFor('/api/pf-mgt/pf-statement/') + pfAccountId + '/' + selectedDate,
      { observe: 'response' }
    );
  }

  protected convertDateFromClient(pfAccount: IPfAccount): IPfAccount {
    const copy: IPfAccount = Object.assign({}, pfAccount, {
      membershipStartDate:
        pfAccount.membershipStartDate && pfAccount.membershipStartDate.isValid()
          ? pfAccount.membershipStartDate.format(DATE_FORMAT)
          : undefined,
      membershipEndDate:
        pfAccount.membershipEndDate && pfAccount.membershipEndDate.isValid() ? pfAccount.membershipEndDate.format(DATE_FORMAT) : undefined,
      dateOfJoining: pfAccount.dateOfJoining && pfAccount.dateOfJoining.isValid() ? pfAccount.dateOfJoining.format(DATE_FORMAT) : undefined,
      dateOfConfirmation:
        pfAccount.dateOfConfirmation && pfAccount.dateOfConfirmation.isValid()
          ? pfAccount.dateOfConfirmation.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.membershipStartDate = res.body.membershipStartDate ? dayjs(res.body.membershipStartDate) : undefined;
      res.body.membershipEndDate = res.body.membershipEndDate ? dayjs(res.body.membershipEndDate) : undefined;
      res.body.dateOfJoining = res.body.dateOfJoining ? dayjs(res.body.dateOfJoining) : undefined;
      res.body.dateOfConfirmation = res.body.dateOfConfirmation ? dayjs(res.body.dateOfConfirmation) : undefined;
    }
    return res;
  }

  public convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pfAccount: IPfAccount) => {
        pfAccount.membershipStartDate = pfAccount.membershipStartDate ? dayjs(pfAccount.membershipStartDate) : undefined;
        pfAccount.membershipEndDate = pfAccount.membershipEndDate ? dayjs(pfAccount.membershipEndDate) : undefined;
        pfAccount.dateOfJoining = pfAccount.dateOfJoining ? dayjs(pfAccount.dateOfJoining) : undefined;
        pfAccount.dateOfConfirmation = pfAccount.dateOfConfirmation ? dayjs(pfAccount.dateOfConfirmation) : undefined;
      });
    }
    return res;
  }
}
