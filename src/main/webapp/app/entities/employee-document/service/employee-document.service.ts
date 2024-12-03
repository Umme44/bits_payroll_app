import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeDocument, NewEmployeeDocument } from '../employee-document.model';

export type PartialUpdateEmployeeDocument = Partial<IEmployeeDocument> & Pick<IEmployeeDocument, 'id'>;

type RestOf<T extends IEmployeeDocument | NewEmployeeDocument> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestEmployeeDocument = RestOf<IEmployeeDocument>;

export type NewRestEmployeeDocument = RestOf<NewEmployeeDocument>;

export type PartialUpdateRestEmployeeDocument = RestOf<PartialUpdateEmployeeDocument>;

export type EntityResponseType = HttpResponse<IEmployeeDocument>;
export type EntityArrayResponseType = HttpResponse<IEmployeeDocument[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeDocumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/employee-document');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  createWithFile(pin: string, file: File, employeeDocument: NewEmployeeDocument): Observable<EntityResponseType> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('employeeDocument', new Blob([JSON.stringify(employeeDocument)], { type: 'application/json' }));

    return this.http.post<NewEmployeeDocument>(`${this.resourceUrl}/${pin}`, formData, { observe: 'response' });
  }

  updateWithFile(pin: string, file: File, employeeDocument: IEmployeeDocument): Observable<EntityResponseType> {
    // const copy = this.convertDateFromClient(employeeDocument);

    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('employeeDocument', new Blob([JSON.stringify(employeeDocument)], { type: 'application/json' }));

    return this.http
      .put<RestEmployeeDocument>(`${this.resourceUrl}/${pin}`, formData, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  updateWithoutFile(pin: string, employeeDocument: IEmployeeDocument): Observable<EntityResponseType> {
    return this.http.put<IEmployeeDocument>(`${this.resourceUrl}/${pin}` + '/without-file', employeeDocument, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeeDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeeDocument[]>(`${this.resourceUrl}`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeDocumentIdentifier(employeeDocument: Pick<IEmployeeDocument, 'id'>): number {
    return employeeDocument.id;
  }

  compareEmployeeDocument(o1: Pick<IEmployeeDocument, 'id'> | null, o2: Pick<IEmployeeDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeDocumentIdentifier(o1) === this.getEmployeeDocumentIdentifier(o2) : o1 === o2;
  }

  addEmployeeDocumentToCollectionIfMissing<Type extends Pick<IEmployeeDocument, 'id'>>(
    employeeDocumentCollection: Type[],
    ...employeeDocumentToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeDocument: Type[] = employeeDocumentToCheck.filter(isPresent);
    if (employeeDocument.length > 0) {
      const employeeDocumentCollectionIdentifiers = employeeDocumentCollection.map(
        employeeDocumentItem => this.getEmployeeDocumentIdentifier(employeeDocumentItem)!
      );
      const employeeDocumentToAdd = employeeDocument.filter(employeeDocumentItem => {
        const employeeDocumentIdentifier = this.getEmployeeDocumentIdentifier(employeeDocumentItem);
        if (employeeDocumentCollectionIdentifiers.includes(employeeDocumentIdentifier)) {
          return false;
        }
        employeeDocumentCollectionIdentifiers.push(employeeDocumentIdentifier);
        return true;
      });
      return [...employeeDocumentToAdd, ...employeeDocumentCollection];
    }
    return employeeDocumentCollection;
  }

  protected convertDateFromClient<T extends IEmployeeDocument | NewEmployeeDocument | PartialUpdateEmployeeDocument>(
    employeeDocument: T
  ): RestOf<T> {
    return {
      ...employeeDocument,
      createdAt: employeeDocument.createdAt?.toJSON() ?? null,
      updatedAt: employeeDocument.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEmployeeDocument: RestEmployeeDocument): IEmployeeDocument {
    return {
      ...restEmployeeDocument,
      createdAt: restEmployeeDocument.createdAt ? dayjs(restEmployeeDocument.createdAt) : undefined,
      updatedAt: restEmployeeDocument.updatedAt ? dayjs(restEmployeeDocument.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeeDocument>): HttpResponse<IEmployeeDocument> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeeDocument[]>): HttpResponse<IEmployeeDocument[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  download(id: number): Observable<HttpResponse<Blob>> {
    const uri = this.resourceUrl + '/download/' + id;
    return this.http.get<Blob>(uri, { responseType: 'blob' as 'json', observe: 'response' });
  }
}
