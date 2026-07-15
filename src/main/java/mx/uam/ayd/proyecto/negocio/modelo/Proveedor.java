package mx.uam.ayd.proyecto.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Proveedor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idProveedor;

	private String razonSocial;

	private String rfc;

	private String contacto;

	@OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final List<OrdenCompra> ordenesCompra = new ArrayList<>();

	public long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public List<OrdenCompra> getOrdenesCompra() {
		return ordenesCompra;
	}

	public void setOrdenesCompra(List<OrdenCompra> ordenesCompra) {
		this.ordenesCompra.clear();
		if (ordenesCompra != null) {
			for (OrdenCompra ordenCompra : ordenesCompra) {
				ordenCompra.setProveedor(this);
			}
			this.ordenesCompra.addAll(ordenesCompra);
		}
	}

	public boolean addOrdenCompra(OrdenCompra ordenCompra) {
		if (ordenCompra == null) {
			throw new IllegalArgumentException("La orden de compra no puede ser null");
		}

		if (ordenesCompra.contains(ordenCompra)) {
			return false;
		}

		ordenCompra.setProveedor(this);
		return ordenesCompra.add(ordenCompra);
	}

	public boolean removeOrdenCompra(OrdenCompra ordenCompra) {
		if (ordenCompra == null) {
			throw new IllegalArgumentException("La orden de compra no puede ser null");
		}

		if (ordenesCompra.remove(ordenCompra)) {
			ordenCompra.setProveedor(null);
			return true;
		}

		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proveedor other = (Proveedor) obj;
		return idProveedor == other.idProveedor;
	}

	@Override
	public int hashCode() {
		return (int) (31 * idProveedor);
	}

	@Override
	public String toString() {
		return "Proveedor [idProveedor=" + idProveedor + ", razonSocial=" + razonSocial
			+ ", rfc=" + rfc + ", contacto=" + contacto + ", ordenesCompra=" + ordenesCompra + "]";
	}
}
