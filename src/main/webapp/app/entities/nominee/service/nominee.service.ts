import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INominee, NewNominee } from '../nominee.model';

export type PartialUpdateNominee = Partial<INominee> & Pick<INominee, 'id'>;

type RestOf<T extends INominee | NewNominee> = Omit<T, 'dateOfBirth' | 'guardianDateOfBirth' | 'nominationDate'> & {
  dateOfBirth?: string | null;
  guardianDateOfBirth?: string | null;
  nominationDate?: string | null;
};

export type RestNominee = RestOf<INominee>;

export type NewRestNominee = RestOf<NewNominee>;

export type PartialUpdateRestNominee = RestOf<PartialUpdateNominee>;

export type EntityResponseType = HttpResponse<INominee>;
export type EntityArrayResponseType = HttpResponse<INominee[]>;

@Injectable({ providedIn: 'root' })
export class NomineeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nominees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nominee: NewNominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nominee);
    return this.http
      .post<RestNominee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(nominee: INominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nominee);
    return this.http
      .put<RestNominee>(`${this.resourceUrl}/${this.getNomineeIdentifier(nominee)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(nominee: PartialUpdateNominee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nominee);
    return this.http
      .patch<RestNominee>(`${this.resourceUrl}/${this.getNomineeIdentifier(nominee)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNominee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNominee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNomineeIdentifier(nominee: Pick<INominee, 'id'>): number {
    return nominee.id;
  }

  compareNominee(o1: Pick<INominee, 'id'> | null, o2: Pick<INominee, 'id'> | null): boolean {
    return o1 && o2 ? this.getNomineeIdentifier(o1) === this.getNomineeIdentifier(o2) : o1 === o2;
  }

  addNomineeToCollectionIfMissing<Type extends Pick<INominee, 'id'>>(
    nomineeCollection: Type[],
    ...nomineesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const nominees: Type[] = nomineesToCheck.filter(isPresent);
    if (nominees.length > 0) {
      const nomineeCollectionIdentifiers = nomineeCollection.map(nomineeItem => this.getNomineeIdentifier(nomineeItem)!);
      const nomineesToAdd = nominees.filter(nomineeItem => {
        const nomineeIdentifier = this.getNomineeIdentifier(nomineeItem);
        if (nomineeCollectionIdentifiers.includes(nomineeIdentifier)) {
          return false;
        }
        nomineeCollectionIdentifiers.push(nomineeIdentifier);
        return true;
      });
      return [...nomineesToAdd, ...nomineeCollection];
    }
    return nomineeCollection;
  }

  protected convertDateFromClient<T extends INominee | NewNominee | PartialUpdateNominee>(nominee: T): RestOf<T> {
    return {
      ...nominee,
      dateOfBirth: nominee.dateOfBirth?.format(DATE_FORMAT) ?? null,
      guardianDateOfBirth: nominee.guardianDateOfBirth?.format(DATE_FORMAT) ?? null,
      nominationDate: nominee.nominationDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restNominee: RestNominee): INominee {
    return {
      ...restNominee,
      dateOfBirth: restNominee.dateOfBirth ? dayjs(restNominee.dateOfBirth) : undefined,
      guardianDateOfBirth: restNominee.guardianDateOfBirth ? dayjs(restNominee.guardianDateOfBirth) : undefined,
      nominationDate: restNominee.nominationDate ? dayjs(restNominee.nominationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNominee>): HttpResponse<INominee> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNominee[]>): HttpResponse<INominee[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
