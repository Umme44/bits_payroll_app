import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPfAccount, NewPfAccount } from '../pf-account.model';
import {IUserPfStatement} from "../../../shared/model/user-pf/user-pf-statement.model";

export type PartialUpdatePfAccount = Partial<IPfAccount> & Pick<IPfAccount, 'id'>;

type RestOf<T extends IPfAccount | NewPfAccount> = Omit<
  T,
  'membershipStartDate' | 'membershipEndDate' | 'dateOfJoining' | 'dateOfConfirmation'
> & {
  membershipStartDate?: string | null;
  membershipEndDate?: string | null;
  dateOfJoining?: string | null;
  dateOfConfirmation?: string | null;
};

export type RestPfAccount = RestOf<IPfAccount>;

export type NewRestPfAccount = RestOf<NewPfAccount>;

export type PartialUpdateRestPfAccount = RestOf<PartialUpdatePfAccount>;

export type EntityResponseType = HttpResponse<IPfAccount>;
export type EntityArrayResponseType = HttpResponse<IPfAccount[]>;

@Injectable({ providedIn: 'root' })
export class PfAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt/pf-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfAccount: NewPfAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfAccount);
    return this.http
      .post<RestPfAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pfAccount: IPfAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfAccount);
    return this.http
      .put<RestPfAccount>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pfAccount: PartialUpdatePfAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfAccount);
    return this.http
      .patch<RestPfAccount>(`${this.resourceUrl}/${this.getPfAccountIdentifier(pfAccount)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPfAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPfAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfAccountIdentifier(pfAccount: Pick<IPfAccount, 'id'>): number {
    return pfAccount.id;
  }

  comparePfAccount(o1: Pick<IPfAccount, 'id'> | null, o2: Pick<IPfAccount, 'id'> | null): boolean {
    return o1 && o2 ? this.getPfAccountIdentifier(o1) === this.getPfAccountIdentifier(o2) : o1 === o2;
  }

  addPfAccountToCollectionIfMissing<Type extends Pick<IPfAccount, 'id'>>(
    pfAccountCollection: Type[],
    ...pfAccountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pfAccounts: Type[] = pfAccountsToCheck.filter(isPresent);
    if (pfAccounts.length > 0) {
      const pfAccountCollectionIdentifiers = pfAccountCollection.map(pfAccountItem => this.getPfAccountIdentifier(pfAccountItem)!);
      const pfAccountsToAdd = pfAccounts.filter(pfAccountItem => {
        const pfAccountIdentifier = this.getPfAccountIdentifier(pfAccountItem);
        if (pfAccountCollectionIdentifiers.includes(pfAccountIdentifier)) {
          return false;
        }
        pfAccountCollectionIdentifiers.push(pfAccountIdentifier);
        return true;
      });
      return [...pfAccountsToAdd, ...pfAccountCollection];
    }
    return pfAccountCollection;
  }

  protected convertDateFromClient<T extends IPfAccount | NewPfAccount | PartialUpdatePfAccount>(pfAccount: T): RestOf<T> {
    return {
      ...pfAccount,
      membershipStartDate: pfAccount.membershipStartDate?.format(DATE_FORMAT) ?? null,
      membershipEndDate: pfAccount.membershipEndDate?.format(DATE_FORMAT) ?? null,
      dateOfJoining: pfAccount.dateOfJoining?.format(DATE_FORMAT) ?? null,
      dateOfConfirmation: pfAccount.dateOfConfirmation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPfAccount: RestPfAccount): IPfAccount {
    return {
      ...restPfAccount,
      membershipStartDate: restPfAccount.membershipStartDate ? dayjs(restPfAccount.membershipStartDate) : undefined,
      membershipEndDate: restPfAccount.membershipEndDate ? dayjs(restPfAccount.membershipEndDate) : undefined,
      dateOfJoining: restPfAccount.dateOfJoining ? dayjs(restPfAccount.dateOfJoining) : undefined,
      dateOfConfirmation: restPfAccount.dateOfConfirmation ? dayjs(restPfAccount.dateOfConfirmation) : undefined,
    };
  }
  protected convertResponseFromServer(res: HttpResponse<RestPfAccount>): HttpResponse<IPfAccount> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }
  protected convertResponseArrayFromServer(res: HttpResponse<RestPfAccount[]>): HttpResponse<IPfAccount[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
  getAllPfAccountsList(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPfAccount[]>(this.resourceUrl + '/list', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  generateUserPfStatement(pfAccountId: number, date: any): Observable<HttpResponse<IUserPfStatement>> {
    const selectedDate = dayjs(date).format(DATE_FORMAT);
    return this.http.get<IUserPfStatement>(SERVER_API_URL + '/api/pf-mgt/pf-statement/' + pfAccountId + '/' + selectedDate, {
      observe: 'response',
    }).pipe(map(res => this.convertUserPfStatementDateFromServer(res)));
  }
  protected convertUserPfStatementDateFromServer(res: HttpResponse<IUserPfStatement>): HttpResponse<IUserPfStatement> {
    if (res.body) {
      res.body.openingBalanceDate = res.body.openingBalanceDate ? dayjs(res.body.openingBalanceDate) : undefined;
      res.body.previousClosingBalanceDate = res.body.previousClosingBalanceDate ? dayjs(res.body.previousClosingBalanceDate) : undefined;
    }
    return res;
  }
}
