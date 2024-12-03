import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPfLoan, NewPfLoan } from '../pf-loan.model';

export type PartialUpdatePfLoan = Partial<IPfLoan> & Pick<IPfLoan, 'id'>;

type RestOf<T extends IPfLoan | NewPfLoan> = Omit<T, 'disbursementDate' | 'instalmentStartFrom'> & {
  disbursementDate?: string | null;
  instalmentStartFrom?: string | null;
};

export type RestPfLoan = RestOf<IPfLoan>;

export type NewRestPfLoan = RestOf<NewPfLoan>;

export type PartialUpdateRestPfLoan = RestOf<PartialUpdatePfLoan>;

export type EntityResponseType = HttpResponse<IPfLoan>;
export type EntityArrayResponseType = HttpResponse<IPfLoan[]>;

@Injectable({ providedIn: 'root' })
export class PfLoanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt/pf-loans');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfLoan: NewPfLoan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoan);
    return this.http
      .post<RestPfLoan>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pfLoan: IPfLoan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoan);
    return this.http
      .put<RestPfLoan>(`${this.resourceUrl}/${this.getPfLoanIdentifier(pfLoan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pfLoan: PartialUpdatePfLoan): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfLoan);
    return this.http
      .patch<RestPfLoan>(`${this.resourceUrl}/${this.getPfLoanIdentifier(pfLoan)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPfLoan>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPfLoan[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfLoanIdentifier(pfLoan: Pick<IPfLoan, 'id'>): number {
    return pfLoan.id;
  }

  comparePfLoan(o1: Pick<IPfLoan, 'id'> | null, o2: Pick<IPfLoan, 'id'> | null): boolean {
    return o1 && o2 ? this.getPfLoanIdentifier(o1) === this.getPfLoanIdentifier(o2) : o1 === o2;
  }

  addPfLoanToCollectionIfMissing<Type extends Pick<IPfLoan, 'id'>>(
    pfLoanCollection: Type[],
    ...pfLoansToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pfLoans: Type[] = pfLoansToCheck.filter(isPresent);
    if (pfLoans.length > 0) {
      const pfLoanCollectionIdentifiers = pfLoanCollection.map(pfLoanItem => this.getPfLoanIdentifier(pfLoanItem)!);
      const pfLoansToAdd = pfLoans.filter(pfLoanItem => {
        const pfLoanIdentifier = this.getPfLoanIdentifier(pfLoanItem);
        if (pfLoanCollectionIdentifiers.includes(pfLoanIdentifier)) {
          return false;
        }
        pfLoanCollectionIdentifiers.push(pfLoanIdentifier);
        return true;
      });
      return [...pfLoansToAdd, ...pfLoanCollection];
    }
    return pfLoanCollection;
  }

  protected convertDateFromClient<T extends IPfLoan | NewPfLoan | PartialUpdatePfLoan>(pfLoan: T): RestOf<T> {
    return {
      ...pfLoan,
      disbursementDate: pfLoan.disbursementDate?.format(DATE_FORMAT) ?? null,
      instalmentStartFrom: pfLoan.instalmentStartFrom?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPfLoan: RestPfLoan): IPfLoan {
    return {
      ...restPfLoan,
      disbursementDate: restPfLoan.disbursementDate ? dayjs(restPfLoan.disbursementDate) : undefined,
      instalmentStartFrom: restPfLoan.instalmentStartFrom ? dayjs(restPfLoan.instalmentStartFrom) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPfLoan>): HttpResponse<IPfLoan> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPfLoan[]>): HttpResponse<IPfLoan[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
