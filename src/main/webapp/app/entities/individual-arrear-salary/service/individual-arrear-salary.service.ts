import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIndividualArrearSalary, NewIndividualArrearSalary } from '../individual-arrear-salary.model';

export type PartialUpdateIndividualArrearSalary = Partial<IIndividualArrearSalary> & Pick<IIndividualArrearSalary, 'id'>;

type RestOf<T extends IIndividualArrearSalary | NewIndividualArrearSalary> = Omit<T, 'effectiveDate'> & {
  effectiveDate?: string | null;
};

export type RestIndividualArrearSalary = RestOf<IIndividualArrearSalary>;

export type NewRestIndividualArrearSalary = RestOf<NewIndividualArrearSalary>;

export type PartialUpdateRestIndividualArrearSalary = RestOf<PartialUpdateIndividualArrearSalary>;

export type EntityResponseType = HttpResponse<IIndividualArrearSalary>;
export type EntityArrayResponseType = HttpResponse<IIndividualArrearSalary[]>;

@Injectable({ providedIn: 'root' })
export class IndividualArrearSalaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/individual-arrear-salaries');
  protected resourceUrlTitleGroup = this.applicationConfigService.getEndpointFor('api/payroll-mgt/individual-arrears-group-by-title');
  protected fileResourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/individual-arrear-salary-export');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(individualArrearSalary: NewIndividualArrearSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individualArrearSalary);
    return this.http
      .post<RestIndividualArrearSalary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(individualArrearSalary: IIndividualArrearSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individualArrearSalary);
    return this.http
      .put<RestIndividualArrearSalary>(this.resourceUrl, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(individualArrearSalary: PartialUpdateIndividualArrearSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individualArrearSalary);
    return this.http
      .patch<RestIndividualArrearSalary>(`${this.resourceUrl}/${this.getIndividualArrearSalaryIdentifier(individualArrearSalary)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIndividualArrearSalary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIndividualArrearSalary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryTitleGroups(): Observable<EntityArrayResponseType> {
    const options = createRequestOption();
    return this.http
      .get<RestIndividualArrearSalary[]>(this.resourceUrlTitleGroup, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryByTitle(title: string, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIndividualArrearSalary[]>(`${this.resourceUrlTitleGroup}/${title}`, {
        params: options,
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  deleteByTitle(title: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrlTitleGroup}/${title}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  public exportIndividualArrearByTitle(title: String): Observable<Blob> {
    //const options = { responseType: 'blob' }; there is no use of this
    const uri = this.fileResourceUrl + '?title=' + encodeURIComponent(String(title));
    // this.http refers to HttpClient. Note here that you cannot use the generic get<Blob> as it does not compile: instead you "choose" the appropriate API in this way.
    return this.http.get(uri, { responseType: 'blob' });
  }

  getIndividualArrearSalaryIdentifier(individualArrearSalary: Pick<IIndividualArrearSalary, 'id'>): number {
    return individualArrearSalary.id;
  }

  compareIndividualArrearSalary(o1: Pick<IIndividualArrearSalary, 'id'> | null, o2: Pick<IIndividualArrearSalary, 'id'> | null): boolean {
    return o1 && o2 ? this.getIndividualArrearSalaryIdentifier(o1) === this.getIndividualArrearSalaryIdentifier(o2) : o1 === o2;
  }

  addIndividualArrearSalaryToCollectionIfMissing<Type extends Pick<IIndividualArrearSalary, 'id'>>(
    individualArrearSalaryCollection: Type[],
    ...individualArrearSalariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const individualArrearSalaries: Type[] = individualArrearSalariesToCheck.filter(isPresent);
    if (individualArrearSalaries.length > 0) {
      const individualArrearSalaryCollectionIdentifiers = individualArrearSalaryCollection.map(
        individualArrearSalaryItem => this.getIndividualArrearSalaryIdentifier(individualArrearSalaryItem)!
      );
      const individualArrearSalariesToAdd = individualArrearSalaries.filter(individualArrearSalaryItem => {
        const individualArrearSalaryIdentifier = this.getIndividualArrearSalaryIdentifier(individualArrearSalaryItem);
        if (individualArrearSalaryCollectionIdentifiers.includes(individualArrearSalaryIdentifier)) {
          return false;
        }
        individualArrearSalaryCollectionIdentifiers.push(individualArrearSalaryIdentifier);
        return true;
      });
      return [...individualArrearSalariesToAdd, ...individualArrearSalaryCollection];
    }
    return individualArrearSalaryCollection;
  }

  protected convertDateFromClient<T extends IIndividualArrearSalary | NewIndividualArrearSalary | PartialUpdateIndividualArrearSalary>(
    individualArrearSalary: T
  ): RestOf<T> {
    return {
      ...individualArrearSalary,
      effectiveDate: individualArrearSalary.effectiveDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restIndividualArrearSalary: RestIndividualArrearSalary): IIndividualArrearSalary {
    return {
      ...restIndividualArrearSalary,
      effectiveDate: restIndividualArrearSalary.effectiveDate ? dayjs(restIndividualArrearSalary.effectiveDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIndividualArrearSalary>): HttpResponse<IIndividualArrearSalary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIndividualArrearSalary[]>): HttpResponse<IIndividualArrearSalary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
