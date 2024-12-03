import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPfNominee, NewPfNominee } from '../pf-nominee.model';

export type PartialUpdatePfNominee = Partial<IPfNominee> & Pick<IPfNominee, 'id'>;

type RestOf<T extends IPfNominee | NewPfNominee> = Omit<T, 'nominationDate' | 'dateOfBirth' | 'guardianDateOfBirth'> & {
  nominationDate?: string | null;
  dateOfBirth?: string | null;
  guardianDateOfBirth?: string | null;
};

export type RestPfNominee = RestOf<IPfNominee>;

export type NewRestPfNominee = RestOf<NewPfNominee>;

export type PartialUpdateRestPfNominee = RestOf<PartialUpdatePfNominee>;

export type EntityResponseType = HttpResponse<IPfNominee>;
export type EntityArrayResponseType = HttpResponse<IPfNominee[]>;

@Injectable({ providedIn: 'root' })
export class PfNomineeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-nominees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pfNominee: NewPfNominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfNominee);
    return this.http
      .post<RestPfNominee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pfNominee: IPfNominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfNominee);
    return this.http
      .put<RestPfNominee>(`${this.resourceUrl}/${this.getPfNomineeIdentifier(pfNominee)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pfNominee: PartialUpdatePfNominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pfNominee);
    return this.http
      .patch<RestPfNominee>(`${this.resourceUrl}/${this.getPfNomineeIdentifier(pfNominee)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPfNominee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPfNominee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfNomineeIdentifier(pfNominee: Pick<IPfNominee, 'id'>): number {
    return pfNominee.id;
  }

  comparePfNominee(o1: Pick<IPfNominee, 'id'> | null, o2: Pick<IPfNominee, 'id'> | null): boolean {
    return o1 && o2 ? this.getPfNomineeIdentifier(o1) === this.getPfNomineeIdentifier(o2) : o1 === o2;
  }

  addPfNomineeToCollectionIfMissing<Type extends Pick<IPfNominee, 'id'>>(
    pfNomineeCollection: Type[],
    ...pfNomineesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pfNominees: Type[] = pfNomineesToCheck.filter(isPresent);
    if (pfNominees.length > 0) {
      const pfNomineeCollectionIdentifiers = pfNomineeCollection.map(pfNomineeItem => this.getPfNomineeIdentifier(pfNomineeItem)!);
      const pfNomineesToAdd = pfNominees.filter(pfNomineeItem => {
        const pfNomineeIdentifier = this.getPfNomineeIdentifier(pfNomineeItem);
        if (pfNomineeCollectionIdentifiers.includes(pfNomineeIdentifier)) {
          return false;
        }
        pfNomineeCollectionIdentifiers.push(pfNomineeIdentifier);
        return true;
      });
      return [...pfNomineesToAdd, ...pfNomineeCollection];
    }
    return pfNomineeCollection;
  }

  protected convertDateFromClient<T extends IPfNominee | NewPfNominee | PartialUpdatePfNominee>(pfNominee: T): RestOf<T> {
    return {
      ...pfNominee,
      nominationDate: pfNominee.nominationDate?.format(DATE_FORMAT) ?? null,
      dateOfBirth: pfNominee.dateOfBirth?.format(DATE_FORMAT) ?? null,
      guardianDateOfBirth: pfNominee.guardianDateOfBirth?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPfNominee: RestPfNominee): IPfNominee {
    return {
      ...restPfNominee,
      nominationDate: restPfNominee.nominationDate ? dayjs(restPfNominee.nominationDate) : undefined,
      dateOfBirth: restPfNominee.dateOfBirth ? dayjs(restPfNominee.dateOfBirth) : undefined,
      guardianDateOfBirth: restPfNominee.guardianDateOfBirth ? dayjs(restPfNominee.guardianDateOfBirth) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPfNominee>): HttpResponse<IPfNominee> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPfNominee[]>): HttpResponse<IPfNominee[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
