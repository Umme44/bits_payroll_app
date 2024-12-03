import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmploymentCertificate, NewEmploymentCertificate } from '../employment-certificate.model';

export type PartialUpdateEmploymentCertificate = Partial<IEmploymentCertificate> & Pick<IEmploymentCertificate, 'id'>;

type RestOf<T extends IEmploymentCertificate | NewEmploymentCertificate> = Omit<
  T,
  'issueDate' | 'createdAt' | 'updatedAt' | 'generatedAt'
> & {
  issueDate?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  generatedAt?: string | null;
};

export type RestEmploymentCertificate = RestOf<IEmploymentCertificate>;

export type NewRestEmploymentCertificate = RestOf<NewEmploymentCertificate>;

export type PartialUpdateRestEmploymentCertificate = RestOf<PartialUpdateEmploymentCertificate>;

export type EntityResponseType = HttpResponse<IEmploymentCertificate>;
export type EntityArrayResponseType = HttpResponse<IEmploymentCertificate[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentCertificateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employment-certificates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employmentCertificate: NewEmploymentCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentCertificate);
    return this.http
      .post<RestEmploymentCertificate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employmentCertificate: IEmploymentCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentCertificate);
    return this.http
      .put<RestEmploymentCertificate>(`${this.resourceUrl}/${this.getEmploymentCertificateIdentifier(employmentCertificate)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employmentCertificate: PartialUpdateEmploymentCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employmentCertificate);
    return this.http
      .patch<RestEmploymentCertificate>(`${this.resourceUrl}/${this.getEmploymentCertificateIdentifier(employmentCertificate)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmploymentCertificate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmploymentCertificate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmploymentCertificateIdentifier(employmentCertificate: Pick<IEmploymentCertificate, 'id'>): number {
    return employmentCertificate.id;
  }

  compareEmploymentCertificate(o1: Pick<IEmploymentCertificate, 'id'> | null, o2: Pick<IEmploymentCertificate, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmploymentCertificateIdentifier(o1) === this.getEmploymentCertificateIdentifier(o2) : o1 === o2;
  }

  addEmploymentCertificateToCollectionIfMissing<Type extends Pick<IEmploymentCertificate, 'id'>>(
    employmentCertificateCollection: Type[],
    ...employmentCertificatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employmentCertificates: Type[] = employmentCertificatesToCheck.filter(isPresent);
    if (employmentCertificates.length > 0) {
      const employmentCertificateCollectionIdentifiers = employmentCertificateCollection.map(
        employmentCertificateItem => this.getEmploymentCertificateIdentifier(employmentCertificateItem)!
      );
      const employmentCertificatesToAdd = employmentCertificates.filter(employmentCertificateItem => {
        const employmentCertificateIdentifier = this.getEmploymentCertificateIdentifier(employmentCertificateItem);
        if (employmentCertificateCollectionIdentifiers.includes(employmentCertificateIdentifier)) {
          return false;
        }
        employmentCertificateCollectionIdentifiers.push(employmentCertificateIdentifier);
        return true;
      });
      return [...employmentCertificatesToAdd, ...employmentCertificateCollection];
    }
    return employmentCertificateCollection;
  }

  protected convertDateFromClient<T extends IEmploymentCertificate | NewEmploymentCertificate | PartialUpdateEmploymentCertificate>(
    employmentCertificate: T
  ): RestOf<T> {
    return {
      ...employmentCertificate,
      issueDate: employmentCertificate.issueDate?.format(DATE_FORMAT) ?? null,
      createdAt: employmentCertificate.createdAt?.toJSON() ?? null,
      updatedAt: employmentCertificate.updatedAt?.toJSON() ?? null,
      generatedAt: employmentCertificate.generatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmploymentCertificate: RestEmploymentCertificate): IEmploymentCertificate {
    return {
      ...restEmploymentCertificate,
      issueDate: restEmploymentCertificate.issueDate ? dayjs(restEmploymentCertificate.issueDate) : undefined,
      createdAt: restEmploymentCertificate.createdAt ? dayjs(restEmploymentCertificate.createdAt) : undefined,
      updatedAt: restEmploymentCertificate.updatedAt ? dayjs(restEmploymentCertificate.updatedAt) : undefined,
      generatedAt: restEmploymentCertificate.generatedAt ? dayjs(restEmploymentCertificate.generatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmploymentCertificate>): HttpResponse<IEmploymentCertificate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmploymentCertificate[]>): HttpResponse<IEmploymentCertificate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
